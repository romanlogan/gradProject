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

    @GetMapping("/validateJwt")
    public ResponseEntity isJwtValidate(HttpServletRequest request) {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email;

        try {
            email = getSubjectInJwt(token);
        } catch (JwtNullTokenException | ExpiredJwtException e) {
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

//    myInfo function is temporarily closed
    // Call validatejwt with ajax to check the token and send it if there is no problem.
    @GetMapping("/myInfo/{token}")
    public String myInfo(@PathVariable("token") String token, Model model) {

        String userEmail;

        try {
            userEmail = getSubjectInJwt(token);
        } catch (ExpiredJwtException e) {
            return "redirect:/loginForm";
        }

        // Get the latest games I've played
//        ResponseMyInfo response = userService.getResponseMyInfo(userEmail);
        ResponseMyInfo response = userService.getResponseMyInfoByKafka("getPlayedGameList-topic", userEmail);       //by Kafka
        model.addAttribute("response", response);
        return "myInfo";
    }

    private String getSubjectInJwt(String token) {

        if (token == null) {
            throw new JwtNullTokenException("Invalid token.");
        }

        String replacedToken = token.replace("Bearer", "");

        if (replacedToken.equals("null")) {
            throw new JwtNullTokenException("Invalid token.");
        }

        String subject = Jwts.parser()
                .setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(replacedToken).getBody()
                .getSubject();

        return subject;
    }

}
