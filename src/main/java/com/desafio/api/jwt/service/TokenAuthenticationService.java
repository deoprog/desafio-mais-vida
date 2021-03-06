/**
 * @see TokenAuthenticationService
 *
 * Será utilizada para fazer a verificação do usuário e senha com base em sua configuração.
 */
package com.desafio.api.jwt.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import javax.ws.rs.core.MediaType;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author deoprog
 */
public class TokenAuthenticationService {

    public static final long EXPIRATIONTIME = 864000000; // 10 dias
    public static final String SECRET = "9@#qweropasdfgtyuihjklzxcvbnm56$";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    
    public static void addAuthentication(HttpServletResponse res, String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);

        try {
            res.getOutputStream().print(ResponseEntity.ok().body(JWT).getBody());
        } catch (IOException e) {
        }
    }

    public static Authentication getByToken(String token) {
        String user = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();

        return user != null ? new UsernamePasswordAuthenticationToken(user, null, null) : null;
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            return getByToken(token);
        }
        return null;
    }

}
