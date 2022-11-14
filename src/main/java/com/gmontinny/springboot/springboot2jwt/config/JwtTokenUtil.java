package com.gmontinny.springboot.springboot2jwt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 5L * 60L * 60L;

    @Value("${jwt.secret}")
    private String secret;

    //recuperar nome de usuário do token jwt
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //recuperar data de expiração do token jwt
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //para recuperar qualquer informação do token, precisaremos da chave secreta
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //verifique se o token expirou
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //gerar token para usuário
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //ao criar o token -
    //1. Defina declarações do token, como Emissor, Expiração, Assunto e o ID
    //2. Assine o JWT usando o algoritmo HS512 e a chave secreta.
    //3. De acordo com a serialização compacta do JWS (https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    // compactação do JWT para uma string segura para URL
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY
                        * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //validar token
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
