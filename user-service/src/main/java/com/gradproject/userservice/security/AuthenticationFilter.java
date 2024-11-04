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

    //  로그인 로직
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            //전달하려는 login 값은이 post 형태로 전달되며
            // post 형태는 requestParameter 로 받을수 없으므로 inputStream 형태로 받아야한다
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            // 비밀번호가 틀리면 에러를 발생
            String email = creds.getEmail();
            String pwd = creds.getPassword();

            UserEntity user = userRepository.findByEmail(email);
            if(user == null){

//                이 에러를 던지면 403 포비던이 뜨는데 화면에 더 명확한 이유를 알려주는 로직이 필요
                throw new NoMatchEmailException("일치하는 이메일이 없습니다");
//                throw new UsernameNotFoundException("일치하는 이메일이 없습니다");
            } else if (
                    !passwordEncoder.matches(pwd, user.getEncryptedPwd())
//                    !user.getEncryptedPwd().equals(pwd)
                    ) {
                // response 를 조정해서 에러를 보내기
                throw new NoMatchPasswordException("일치하는 비밀번호가 없습니다.");
            }

            //
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

    // 실제 로그인 성공시, 어떤 처리를 할지 (토큰 생성, 만료시간 설정, 로그인시 반환값)
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        String username = ((User) authentication.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(username);

        String token = Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        response.addHeader("token", token);
//        response.addHeader("userId", userDetails.getUserId());
    }
}

