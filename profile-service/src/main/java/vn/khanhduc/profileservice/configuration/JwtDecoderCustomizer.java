package vn.khanhduc.profileservice.configuration;

import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "JWT-DECODER")
public class JwtDecoderCustomizer implements JwtDecoder {

    @Override
    public Jwt decode(String token) throws JwtException {
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
