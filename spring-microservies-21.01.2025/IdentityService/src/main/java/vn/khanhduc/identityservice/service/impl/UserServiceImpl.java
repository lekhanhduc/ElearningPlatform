package vn.khanhduc.identityservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import vn.khanhduc.identityservice.common.UserStatus;
import vn.khanhduc.identityservice.dto.request.ProfileCreateRequest;
import vn.khanhduc.identityservice.dto.request.UserCreationRequest;
import vn.khanhduc.identityservice.dto.response.PageResponse;
import vn.khanhduc.identityservice.dto.response.UserCreationResponse;
import vn.khanhduc.identityservice.dto.response.UserDetailResponse;
import vn.khanhduc.identityservice.dto.response.UserProfileResponse;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.model.User;
import vn.khanhduc.identityservice.repository.UserRepository;
import vn.khanhduc.identityservice.repository.httpclient.ProfileClient;
import vn.khanhduc.identityservice.service.UserService;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestClient restClient;
    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final ProfileClient profileClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCreationResponse createUser(UserCreationRequest request) {
        log.info("User creation");
        if(userRepository.existsByEmail(request.getEmail())) {
            log.error("User already exists {}", request.getEmail());
            throw new IdentityException(ErrorCode.USER_EXISTED);
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userStatus(UserStatus.ACTIVE)
                .build();
        userRepository.save(user);
        log.info("User created");

        var response = profileClient.createProfile(ProfileCreateRequest.builder()
                        .userId(user.getId())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .phoneNumber(null)
                .build());

        log.info("Profile response {}", response);

        log.info("Profile created");

        // Can send email here with Kafka

        return UserCreationResponse.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullName(String.format("%s %s", request.getFirstName(), request.getLastName()))
                .email(user.getEmail())
                .build();
    }

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
    public UserProfileResponse getUserProfileByIdWithOpenFeign(String id) {
        log.info("Get user profile with open feign");
        return profileClient.getUserProfile(id);
    }

}
