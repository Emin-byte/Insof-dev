package io.munkush.app.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtGenerator {

    private String jwtSecret =  "secret";

    private long jwtExpired = 4*60*1000;


    public boolean isTokenExpires(String token){

        var expiresAt = getDecodedJwt(token).getExpiresAt();
        Date nowDate = new Date(System.currentTimeMillis());

        long expiresInMilliseconds = expiresAt.getTime() - nowDate.getTime();
        long threeDaysInMillis = 3 * 24 * 60 * 60 * 1000;

        return expiresInMilliseconds <= threeDaysInMillis;
    }


    public String createRefresh(String username){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpired + 500000))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(jwtSecret.getBytes()));
    }

    public String createAccess(String username){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpired))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(jwtSecret.getBytes()));

    }

    public boolean isTokenExpired(String token){
        DecodedJWT decodedJwt = getDecodedJwt(token);
        var expiresAt = decodedJwt.getExpiresAt();
        return expiresAt.before(new Date());
    }

    public String getUsername(String token){
        if(token.isEmpty() || token.isBlank())
            throw new IllegalStateException("Токен пустой");
        var decodedJwt = getDecodedJwt(token);
        return decodedJwt.getSubject();
    }
    private DecodedJWT getDecodedJwt(String token){
        var algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        var build = JWT.require(algorithm).build();
        try {
            return build.verify(token);
        } catch (Exception e){
            throw new IllegalStateException("token is not valid");
        }
    }
}