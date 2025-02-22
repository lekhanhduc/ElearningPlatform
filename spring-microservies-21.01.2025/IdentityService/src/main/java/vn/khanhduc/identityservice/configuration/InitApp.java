package vn.khanhduc.identityservice.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.khanhduc.identityservice.common.UserStatus;
import vn.khanhduc.identityservice.common.UserType;
import vn.khanhduc.identityservice.dto.request.ProfileCreateRequest;
import vn.khanhduc.identityservice.model.Role;
import vn.khanhduc.identityservice.model.User;
import vn.khanhduc.identityservice.model.UserHasRole;
import vn.khanhduc.identityservice.repository.RoleRepository;
import vn.khanhduc.identityservice.repository.UserHasRoleRepository;
import vn.khanhduc.identityservice.repository.UserRepository;
import vn.khanhduc.identityservice.repository.httpclient.ProfileClient;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "APP-INIT")
public class InitApp {

    private final String EMAIL_ADMIN = "admin@gmail.com";
    private final String PASSWORD_ADMIN = "123456";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserHasRoleRepository userHasRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileClient profileClient;

    @Bean
    @ConditionalOnProperty(prefix = "spring", value = "driver-class-name", havingValue = "com.mysql.cj.jdbc.Driver")
    public ApplicationRunner init() {
        log.info("Initializing application");
        return args -> {
            if(userRepository.findByEmail(EMAIL_ADMIN).isEmpty()) {
                Optional<Role> roleAdmin = roleRepository.findByName(String.valueOf(UserType.ADMIN));
                if(roleAdmin.isEmpty()) {
                    roleRepository.save(Role.builder().name(String.valueOf(UserType.ADMIN)).build());
                }
                Optional<Role> userRole = roleRepository.findByName(String.valueOf(UserType.USER));
                if(userRole.isEmpty()) {
                    roleRepository.save(Role.builder().name(String.valueOf(UserType.USER)).build());
                }

                User user = new User();
                user.setEmail(EMAIL_ADMIN);
                user.setPassword(passwordEncoder.encode(PASSWORD_ADMIN));
                user.setUserStatus(UserStatus.ACTIVE);

                UserHasRole userHasRole = new UserHasRole();
                userHasRole.setRole(roleAdmin.get());
                userHasRole.setUser(user);

                userRepository.save(user);

                profileClient.createProfile(ProfileCreateRequest.builder()
                                .userId(user.getId())
                                .firstName("Admin")
                                .lastName("system")
                                .phoneNumber("0368103455")
                                .avatar(null)
                        .build());

                log.info("Init application finished");
            }
        };
    }
}
