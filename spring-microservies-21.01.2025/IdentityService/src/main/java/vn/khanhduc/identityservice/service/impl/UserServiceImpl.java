package vn.khanhduc.identityservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import vn.khanhduc.event.dto.NotificationEvent;
import vn.khanhduc.identityservice.common.UserStatus;
import vn.khanhduc.identityservice.common.UserType;
import vn.khanhduc.identityservice.dto.request.ProfileCreateRequest;
import vn.khanhduc.identityservice.dto.request.UserCreationRequest;
import vn.khanhduc.identityservice.dto.response.UserCreationResponse;
import vn.khanhduc.identityservice.dto.response.UserDetailResponse;
import vn.khanhduc.identityservice.dto.response.UserProfileResponse;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.model.Role;
import vn.khanhduc.identityservice.model.User;
import vn.khanhduc.identityservice.model.UserHasRole;
import vn.khanhduc.identityservice.repository.RoleRepository;
import vn.khanhduc.identityservice.repository.UserRepository;
import vn.khanhduc.identityservice.repository.httpclient.ProfileClient;
import vn.khanhduc.identityservice.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import static vn.khanhduc.identityservice.common.Channel.EMAIL;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestClient restClient;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final RoleRepository roleRepository;
    private final ProfileClient profileClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCreationResponse createUser(UserCreationRequest request) {
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

           var profileRequest = ProfileCreateRequest.builder()
                   .userId(user.getId())
                   .firstName(request.getFirstName())
                   .lastName(request.getLastName())
                   .phoneNumber(null)
                   .avatar(null)
                   .build();

           profileClient.createProfile(profileRequest);
           log.info("Profile created");

           var param = new HashMap<String, Object>();
           param.put("name", String.format("%s %s", request.getFirstName(), request.getLastName()));
           param.put("subject", "Welcome to Bookstore");
           param.put("body", "Hello " + request.getFirstName() + " " + request.getLastName());
           NotificationEvent event = NotificationEvent.builder()
                   .channel(EMAIL)
                   .recipient(user.getEmail())
                   .param(param)
                   .build();

           kafkaTemplate.send("user-onboard-success", event);

       } catch (DataIntegrityViolationException e) {
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
    public UserProfileResponse getUserProfileByIdWithRestClient(String id) {
        log.info("Get user profile with rest client");
        return restClient.get()
                .uri("http://localhost:8081/api/v1/profiles/" + id)
                .retrieve()
                .body(UserProfileResponse.class);
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

}
