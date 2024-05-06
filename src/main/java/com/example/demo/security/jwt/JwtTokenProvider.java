package com.example.demo.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(UserDetailsImpl userDetails){
        String[] claims = getClaimsFromUser(userDetails);

        return JWT.create()
                .withIssuer("GET ARRAYS")
                .withAudience("USER MANAGEMENT PORTAL")
                .withIssuedAt(new Date()).withSubject(userDetails.getUsername())
                .withArrayClaim("Authorities",claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 432_000_000))
                .sign(Algorithm.HMAC512(jwtSecret.getBytes()));
    }

    public List<GrantedAuthority> getAuthorities(String token){
        String[] claims = getClaimsFromToken(token);

        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public String[] getClaimsFromToken(String token){
        JWTVerifier verifier = getJWTVerifier();

        return verifier.verify(token).getClaim("USER MANAGEMENT PORTAL").asArray(String.class);
    }

    public Authentication getAuthentication(String username,
                                            List<GrantedAuthority> authorities,
                                            HttpServletRequest request
                                            ){

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username,null,authorities);

        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return usernamePasswordAuthenticationToken;
    }

    public boolean isTokenValid(String username,String token){
        JWTVerifier verifier = getJWTVerifier();
        return !StringUtils.isEmpty(username) && !isTokenExpired(verifier,token);
    }

    public String getSubject(String token){
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
    }

    public boolean isTokenExpired(JWTVerifier verifier,String token){
        Date expiration = verifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    private JWTVerifier getJWTVerifier(){
        JWTVerifier verifier;
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            verifier = JWT.require(algorithm).withIssuer("ARRAYS").build();
        }catch (JWTVerificationException ex){
            throw new JWTVerificationException("Token cannot be verified");
        }
        return verifier;
    }
    private String[] getClaimsFromUser(UserDetailsImpl userDetails){
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority: userDetails.getAuthorities()){
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }
}



















