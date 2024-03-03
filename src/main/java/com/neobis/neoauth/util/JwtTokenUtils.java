package com.neobis.neoauth.util;

import com.neobis.neoauth.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {

    private static final String ACCESS_SECRET_KEY = "12638ab829cd93729ef9327340291cd8484299f438362ba6";
    private static final String REFRESH_SECRET_KEY = "5555ab829cd93729ef9374628291cd8484299f438362ba67";

    private static SecretKey getAccessKey() {
        return Keys.hmacShaKeyFor(ACCESS_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static SecretKey getRefreshKey() {
        return Keys.hmacShaKeyFor(REFRESH_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roleList = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roleList);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(Instant.now().plus(2, ChronoUnit.MINUTES).toEpochMilli()))
                .signWith(getAccessKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli()))
                .signWith(getRefreshKey())
                .compact();
    }


    public String getUsername(String token) {
        return  Jwts.parser()
                .verifyWith(getAccessKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public String getUsernameFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .verifyWith(getRefreshKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload()
                .getSubject();
    }
    private Claims getAllClaimsFromToken(String token){
        return Jwts
                .parser()
                .setSigningKey(getAccessKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Boolean validateToken(String token, UserDetails user) {
        final String username = getUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
