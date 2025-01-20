package vn.khanhduc.identityservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.khanhduc.identityservice.dto.request.UserCreationRequest;
import vn.khanhduc.identityservice.dto.response.UserCreationResponse;
import vn.khanhduc.identityservice.dto.response.UserDetailResponse;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.model.User;
import vn.khanhduc.identityservice.repository.UserRepository;
import vn.khanhduc.identityservice.service.UserService;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER-SERVICE")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserCreationResponse createUser(UserCreationRequest request) {
        log.info("User creation");
        if(userRepository.existsByEmail(request.getEmail())) {
            log.error("User already exists {}", request.getEmail());
            throw new IdentityException(ErrorCode.USER_EXISTED);
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        userRepository.save(user);
        log.info("User created");
        return UserCreationResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(String.format("%s %s", user.getFirstName(), user.getLastName()))
                .email(user.getEmail())
                .build();
    }

    @Override
    public List<UserDetailResponse> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserDetailResponse.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .fullName(String.format("%s %s", user.getFirstName(), user.getLastName()))
                        .email(user.getEmail())
                        .phone(user.getPhoneNumber())
                        .build())
                .toList();
    }

}
