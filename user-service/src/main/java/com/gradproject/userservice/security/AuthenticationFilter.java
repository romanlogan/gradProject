package com.gradproject.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gradproject.userservice.dto.RequestLogin;
import com.gradproject.userservice.dto.UserDto;
import com.gradproject.userservice.entity.UserEntity;
import com.gradproject.userservice.exception.NoMatchEmailException;
import com.gradproject.userservice.exception.NoMatchPasswordException;
import com.gradproject.userservice.repository.UserRepository;
import com.gradproject.userservice.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UserRepository userRepository;
    private UserService userService;
    private Environment env;
    private PasswordEncoder passwordEncoder;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService,
                                UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                Environment env) {

        super.setAuthenticationManager(authenticationManager);
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String expTime = env.getProperty("token.expiration_time");
        try {
            //The login data is transmitted in the form of post.
            // Since the post form cannot be received as a request parameter, it must be received in the form of inputStream.
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            String email = creds.getEmail();
            String pwd = creds.getPassword();

            UserEntity user = userRepository.findByEmail(email);

            if(user == null){
                throw new NoMatchEmailException("No matching emails found.");
            }
            else if (!passwordEncoder.matches(pwd, user.getEncryptedPwd())) {
                throw new NoMatchPasswordException("No matching password found.");
            }

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    //  If login is successful, return jwt
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        String expTime = env.getProperty("token.expiration_time");

        String username = ((User) authentication.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(username);

        String token = Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        response.addHeader("token", token);
    }
}

