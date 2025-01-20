package vn.khanhduc.identityservice.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import vn.khanhduc.identityservice.exception.ErrorCode;
import vn.khanhduc.identityservice.exception.IdentityException;
import vn.khanhduc.identityservice.model.User;
import vn.khanhduc.identityservice.repository.UserRepository;
import vn.khanhduc.identityservice.service.JwtService;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JWT-DECODER")
public class JwtDecoderCustomizer implements JwtDecoder {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        if(Objects.isNull(nimbusJwtDecoder)) {
            SecretKey key = new SecretKeySpec(secretKey.getBytes(), JWSAlgorithm.HS512.toString());
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(key)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        String email = jwtService.ExtractUserName(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IdentityException(ErrorCode.USER_NOT_EXISTED));
        try {
            boolean isValid = jwtService.verificationToken(token, user);
            if(isValid) {
                return nimbusJwtDecoder.decode(token);
            }
        } catch (ParseException | JOSEException e) {
            log.error("Jwt decoder: Token invalid");
            throw new IdentityException(ErrorCode.TOKEN_INVALID);
        }
        throw new JwtException("Invalid token");
    }
}
