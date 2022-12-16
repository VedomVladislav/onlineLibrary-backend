package by.vedom.library.auth.utils;

import by.vedom.library.auth.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Component
@Log
public class JwtUtils {

    private User user;

    public static final String CLAIM_USER_KEY = "user";
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access_token-expiration}")
    private int accessTokenExpiration;

    @Value("${jwt.reset-pass-expiration}")
    private int resetPassTokenExpiration;


    public String createAccessToken(User user) {
        return createToken(user, accessTokenExpiration);
    }

    public String createEmailResetToken(User user) {
        return createToken(user, resetPassTokenExpiration);
    }

    public String createToken(User user, int duration) {
        user.setPassword(null);
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

//        if (duration == 1) {
//            calendar.add(Calendar.DATE, 1);
//        } else {
//            calendar.add(Calendar.MINUTE, 5);
//        }

        calendar.add(Calendar.SECOND, duration);

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_KEY, user);
        claims.put(Claims.SUBJECT, user.getId());

//        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(currentDate)
//                .setExpiration(calendar.getTime())
//
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + duration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validate(String jwt) {
        try {

            // все способы работы с библиотекой io.jsonwebtoken можно смотреть на странице https://github.com/jwtk/jjwt


            // проверка подписи "секретным ключом" и получение нужных значений из payload
            Map map = (Map) Jwts.parser().setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                            .parseClaimsJws(jwt).getBody().
                            get(CLAIM_USER_KEY);

            ObjectMapper mapper = new ObjectMapper();
            this.user = mapper.convertValue(map, User.class);


            return true;
        } catch (MalformedJwtException e) {
            log.log(Level.SEVERE, "Invalid JWT token: ", jwt);
        } catch (ExpiredJwtException e) {
            log.log(Level.SEVERE, "JWT token is expired: ", jwt);
        } catch (UnsupportedJwtException e) {
            log.log(Level.SEVERE, "JWT token is unsupported: ", jwt);
        } catch (IllegalArgumentException e) {
            log.log(Level.SEVERE, "JWT claims string is empty: ", jwt);
        }

        return false;
    }

//    public User getUser(String jwt) {
//        Map map = (Map) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().get(CLAIM_USER_KEY);
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.convertValue(map, User.class);
//    }

    public User getUser() {
        return user;
    }
}
