package com.gradproject.userservice.security;

import com.gradproject.userservice.repository.UserRepository;
import com.gradproject.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserRepository userRepository;
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    public WebSecurity(UserService userService,
                       UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       Environment env) {

        this.env = env;
        this.userService = userService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        //h2 console 을 로그인 하기위해 일단 그대로 둔다, 나중에 mysql 로 옮기면 변경하기
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.authorizeRequests().mvcMatchers("/","/login","/registration","/loginForm","/test","/users", "/img/**", "/css/**","/main","/myInfo/**","/validateJwt").permitAll();
        http.authorizeRequests().antMatchers("/**")
                .permitAll()
//                .hasIpAddress("127.0.0.1")
                .and()
                .addFilter(getAuthenticationFilter());

        //모든 요청중 지정된 IP 주소 와 filter 를 통과한 요청만 성공
        http.headers().frameOptions().disable();
    }



//    bean 으로 등록하여 사용하는것이 아니라 스프링 시큐리티에서 이 인스턴스를 직접 생성하여 사용
    private AuthenticationFilter getAuthenticationFilter() throws Exception {

        AuthenticationFilter authenticationFilter
                = new AuthenticationFilter(authenticationManager(),userService,userRepository,bCryptPasswordEncoder,env);
//        authenticationFilter.setAuthenticationManager(authenticationManager());

        return authenticationFilter;
    }


//
//
//    //select pwd from users where email=?
//    // dp_pwd(enc) == input_pwd(enc)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//        변환 처리
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }
}
