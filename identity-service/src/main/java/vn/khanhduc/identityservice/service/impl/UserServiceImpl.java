package vn.khanhduc.identityservice.service.impl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import vn.khanhduc.event.dto.ProfileEvent;
import vn.khanhduc.identityservice.common.SecurityUtils;
import vn.khanhduc.identityservice.common.UserStatus;
import vn.khanhduc.identityservice.common.UserType;
import vn.khanhduc.identityservice.dto.request.ChangePasswordRequest;
import vn.khanhduc.identityservice.dto.request.UserCreationRequest;
import vn.khanhduc.identityservice.dto.response.UserCreationResponse;
import vn.khanhduc.identityservice.dto.response.UserDetailResponse;
import vn.khanhduc.identityservice.dto.response.UserProfileResponse;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.entity.Role;
import vn.khanhduc.identityservice.entity.User;
import vn.khanhduc.identityservice.entity.UserHasRole;
import vn.khanhduc.identityservice.repository.RoleRepository;
import vn.khanhduc.identityservice.repository.UserRepository;
import vn.khanhduc.identityservice.repository.httpclient.ProfileClient;
import vn.khanhduc.identityservice.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final RoleRepository roleRepository;
    private final ProfileClient profileClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCreationResponse createUser(UserCreationRequest request) {
        log.info("User creation ...");
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userStatus(UserStatus.ACTIVE)
                .build();

        Role role = roleRepository.findByName(String.valueOf(UserType.USER))
                        .orElseGet(() -> roleRepository.save(Role.builder().name(String.valueOf(UserType.USER)).build()));
        UserHasRole userHasRole = UserHasRole.builder().role(role).user(user).build();
        user.setUserHasRoles(Set.of(userHasRole));
       try {
           userRepository.save(user);
           log.info("User created");

           ProfileEvent profileEvent = ProfileEvent.builder()
                   .userId(user.getId())
                   .email(user.getEmail())
                   .firstName(request.getFirstName())
                   .lastName(request.getLastName())
                   .phoneNumber(null)
                   .avatar(null)
                   .build();

           kafkaTemplate.send("user-created", profileEvent);

       } catch (DataIntegrityViolationException e) {
           log.error("User already existed");
           throw new IdentityException(ErrorCode.USER_EXISTED);
       }
       return UserCreationResponse.builder()
               .firstName(request.getFirstName())
               .lastName(request.getLastName())
               .fullName(String.format("%s %s", request.getFirstName(), request.getLastName()))
               .email(user.getEmail())
               .build();
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    @Override
    public List<UserDetailResponse> getAllUser(int page, int size) {
        log.info("Get all user");
        var profiles = profileClient.getAllProfile(page, size);

        return userRepository.findAll()
                .stream()
                .map(user -> UserDetailResponse.builder()
                        .firstName(profiles.getFirstName())
                        .lastName(profiles.getLastName())
                        .fullName(String.format("%s %s", profiles.getFirstName(), profiles.getLastName()))
                        .email(user.getEmail())
                        .phone(profiles.getPhoneNumber())
                        .build())
                .toList();
    }

    @Override
    @CircuitBreaker(name = "", fallbackMethod = "fallBackGetProfileByUserId")
    public UserDetailResponse getUserById(Long id) {
        var profile = profileClient.getProfileByUserId(id);
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IdentityException(ErrorCode.USER_NOT_EXISTED));

        return UserDetailResponse.builder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .fullName(String.format("%s %s", profile.getFirstName(), profile.getLastName()))
                .email(user.getEmail())
                .build();
    }


    @Override
    public UserProfileResponse getUserProfileByIdWithRestTemplate(String id) {
        log.info("Get user profile with rest template");
        return restTemplate.getForObject("http://localhost:8081/api/v1/profiles/" + id, UserProfileResponse.class);
        // post : postForObject
    }

    @Override
    public UserProfileResponse getUserProfileByIdWithWebClient(String id) {
        log.info("Get user profile with web client");
        return webClient.method(HttpMethod.GET)
                .uri("http://localhost:8081/api/v1/profiles/" + id)
                .retrieve()
                .bodyToMono(UserProfileResponse.class) // bodyToMono: lấy đơn, bodyToFlux: một hoặc nhiều
                .block(); // thực thi đồng bộ, bất đồng bộ thì dùng Mono<UserProfileResponse>
    }

    @Override
    public UserProfileResponse getUserProfileByIdWithOpenFeign(Long id) {
        log.info("Get user profile with open feign");
        return profileClient.getUserProfile(id);
    }

    @Override
    @CircuitBreaker(name = "IDENTITY-SERVICE", fallbackMethod = "fallBackGetProfileByUserId")
    public UserProfileResponse getInfoUserLogin() {
        Optional<String> principal = SecurityContextHolder.getContext().getAuthentication().getName().describeConstable();
        if(principal.isEmpty()) {
            throw new IdentityException(ErrorCode.UNAUTHENTICATED);
        }
        Long userId = Long.valueOf(principal.get());
        return profileClient.getProfileByUserId(userId);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        var userId = SecurityUtils.getCurrentUserLogin();
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IdentityException(ErrorCode.USER_NOT_EXISTED));

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            log.error("Old password not match");
            throw new IdentityException(ErrorCode.OLD_PASSWORD_NOT_MATCH);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Change password success");
    }

    private UserProfileResponse fallBackGetProfileByUserId(Throwable throwable) {
        log.error("Fallback triggered for getInfoUserLogin with user login {} ", throwable.getMessage());
        throw new IdentityException(ErrorCode.PROFILE_SERVICE_UNAVAILABLE);
    }

}
