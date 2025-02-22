package vn.khanhduc.identityservice.configuration;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import vn.khanhduc.identityservice.service.JwtService;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JWT-DECODER")
public class JwtDecoderCustomizer implements JwtDecoder {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final JwtService jwtService;
    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        // Kiểm tra tính hợp lệ của token trước khi giải mã
//        try {
//            jwtService.verificationToken(token);
//        } catch (JOSEException | ParseException e) {
//            throw new JwtException(e.getMessage());
//        }
//        if(Objects.isNull(nimbusJwtDecoder)) {
//            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), JWSAlgorithm.HS256.toString());
//            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(keySpec)
//                    .macAlgorithm(MacAlgorithm.HS256)
//                    .build();
//        }
//        return nimbusJwtDecoder.decode(token); // việc này đã được API Gate Way gọi api introspect để kiểm tra nên việc kiểm tra trên là không cần thiết
//    }
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            var header = signedJWT.getHeader().toJSONObject();
            var issuedAt = signedJWT.getJWTClaimsSet().getIssueTime().toInstant();
            var expiresAt = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            var claims = signedJWT.getJWTClaimsSet().getClaims();
            return new Jwt(token, issuedAt, expiresAt, header, claims);
        } catch (ParseException e) {
            throw new JwtException("Invalid token", e);
        }
    }
}
