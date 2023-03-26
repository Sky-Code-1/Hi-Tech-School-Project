package org.flexicode.hitechschool.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.flexicode.hitechschool.users.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.parserBuilder;

@Service
@RequiredArgsConstructor
public class AppJwtService{
    private final UserRepository repository;
    private final String KEY = "4D635166546A576E5A7234753778214125442A462D4A614E645267556B587032";
    private final Key SIGNING_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));

    public Claims extractAllClaims(String token){
        return parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token).getBody();
    }
    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    public String generateToken(Map<String, Object> extraClaims, UserDetails users){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(users.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (60*60*24*1000)))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateToken(UserDetails user){
        return generateToken(new HashMap<>(), user);
    }
    public boolean isUserValid(String token, UserDetails user){
        return extractUsername(token).equalsIgnoreCase(user.getUsername())
                && extractExpiration(token).after(new Date());
    }
}
