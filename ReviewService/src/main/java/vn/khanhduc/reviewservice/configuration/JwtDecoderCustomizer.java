package vn.khanhduc.reviewservice.configuration;

import com.nimbusds.jwt.SignedJWT;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import java.text.ParseException;

@Component
public class JwtDecoderCustomizer implements JwtDecoder {

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            var issuedAt = signedJWT.getJWTClaimsSet().getIssueTime().toInstant();
            var header = signedJWT.getHeader().toJSONObject();
            var expiresAt = signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            var claims = signedJWT.getJWTClaimsSet().getClaims();
            return new Jwt(token, issuedAt, expiresAt, header, claims);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
