package com.todolist.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.todolist.exception.JWTCreationException;
import com.todolist.exception.JWTVerificationException;
import com.todolist.model.User;

import java.util.Date;
import java.util.Calendar;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (Exception e) {
            throw new JWTCreationException("Error generating token", e);
        }
    }
    
    public String validateToken(String token) {
    	try {
    		 Algorithm algorithm = Algorithm.HMAC256(secret);
    		 return JWT.require(algorithm)
                     .withIssuer("login-auth-api")
                     .build()
                     .verify(token)
                     .getSubject();
           
    	}catch (JWTVerificationException e) {
    		throw new JWTCreationException("Error : User not Autenticate", e);
		}
    }
    

    private Date generateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 5); // Adiciona 5 horas à data atual
        return calendar.getTime();
    }
}
