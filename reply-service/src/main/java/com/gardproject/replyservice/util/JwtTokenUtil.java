package com.gardproject.replyservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public static String generateMockToken(String email) {

        String SECRET_KEY = "kyle";

        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(String.valueOf(EXPIRATION_TIME))))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        return token;
    }
}