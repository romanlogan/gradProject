package com.gradproject.userservice.controller;

import com.gradproject.userservice.binding.CheckBindingResult;
import com.gradproject.userservice.dto.RequestUser;
import com.gradproject.userservice.dto.ResponseMyInfo;
import com.gradproject.userservice.dto.ResponseUser;
import com.gradproject.userservice.dto.UserDto;
import com.gradproject.userservice.exception.DuplicateEmailException;
import com.gradproject.userservice.exception.JwtNullTokenException;
import com.gradproject.userservice.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.micrometer.core.annotation.Timed;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    private Environment env;

    private UserService userService;

    @Autowired
    public UserController(Environment env, UserService userService) {
        this.env = env;
        this.userService = userService;
//        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/main")
    public String main() {

        return "main";
    }

    @GetMapping("/registration")
    public String getRegistrationForm() {

        return "registerForm";
    }

    @GetMapping("/loginForm")
    public String getLoginForm() {

        return "loginForm";
    }

//    @GetMapping("/welcome")
//    public String welcome(){
//        return "welcome to user service";
//    }

    @ResponseBody
    @GetMapping("/health_check")
    @Timed(value = "users.status",longTask = true)
    public String status( Authentication authentication) {

        if (authentication == null) {
            System.out.println("authenticatino is null");
        }
        else {
            System.out.println(((User) authentication.getPrincipal()).getUsername());
        }

        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", token secret=" + env.getProperty("token.secret")
                + ", token expiration time=" + env.getProperty("token.expiration_time")
        );
    }

    @GetMapping("/validateJwt")
    public ResponseEntity isJwtValidate(HttpServletRequest request) {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        //token 검증 로직
        String email;

        try {
            email = getSubjectInJwt(token);
        } catch (JwtNullTokenException e) {

            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
//            return (ResponseEntity) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {

            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        UserDto userDto = userService.getUserDetailsByEmail(email);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("loggedInUserId", userDto.getEmail());

        return new ResponseEntity(responseData, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody @Valid RequestUser request,
                                     BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        Long id = null;

        try {
            id = userService.createUser(UserDto.create(request));
        } catch (DuplicateEmailException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(id);

    }

//    ajax 로 validatejwt 를 호출해서 token 검사후 문제없으면 보내기
    @GetMapping("/myInfo/{token}")
    public String myInfo(@PathVariable("token") String token, Model model) {

        String userEmail;

        try {
            userEmail = getSubjectInJwt(token);
        } catch (ExpiredJwtException e) {

//            redirect 로 보내면 로그인으로 보내야된다
            return "redirect:/loginForm";
        }

        // 내가 플레이 한 게임중 최신의 게임을 가져오기
//        ResponseMyInfo response = userService.getResponseMyInfo(userEmail);
        ResponseMyInfo response = userService.getResponseMyInfoByKafka("getPlayedGameList-topic",userEmail);



        model.addAttribute("response", response);
        return "myInfo";
    }



    private String getSubjectInJwt(String token) {

        if (token == null) {
            throw new JwtNullTokenException("유효하지 않은 token 입니다. (null)");
        }

        String replacedToken = token.replace("Bearer", "");

        if (replacedToken.equals("null")) {
            throw new JwtNullTokenException("유효하지 않은 token 입니다. (null string token)");
        }

        String subject = Jwts.parser()
                .setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(replacedToken).getBody()
                .getSubject();

        return subject;
    }

}
