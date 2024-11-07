package com.gradproject.commentservice.controller;

import com.gradproject.commentservice.binding.CheckBindingResult;
import com.gradproject.commentservice.dto.RequestDelete;
import com.gradproject.commentservice.dto.RequestSaveComment;
import com.gradproject.commentservice.dto.RequestUpdate;
import com.gradproject.commentservice.dto.ResponseCommentList;
import com.gradproject.commentservice.exception.JwtNullTokenException;
import com.gradproject.commentservice.exception.UserCommentAlreadyExistException;
import com.gradproject.commentservice.service.CommentService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@Slf4j
public class CommentController {

    private CommentService commentService;

    private Environment env;

    @Autowired
    public CommentController(CommentService commentService, Environment env) {
        this.commentService = commentService;
        this.env = env;
    }

    @PostMapping("/save")
    public ResponseEntity save(HttpServletRequest httpServletRequest,
                               @RequestBody @Valid RequestSaveComment saveRequest,
                               BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        String userEmail;
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            userEmail = getSubjectInJwt(token);
        } catch (JwtNullTokenException | ExpiredJwtException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        Long id = null;

        try {
            id = commentService.save(saveRequest, userEmail);
        } catch (UserCommentAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return new ResponseEntity(id, HttpStatus.OK);
    }


//    @PostMapping("/save/{token}")
//    public ResponseEntity save(@PathVariable("token") String token,
//                               Model model,
//                               @RequestBody RequestSaveComment request) {
//
//        //        expireJWTException 처리 필요
//
//        //jwt 에서 현재 로그인 한 유저 email 뽑기
//        String userEmail;
//        try {
//            userEmail = getSubjectInJwt(token);
//        } catch (JwtNullTokenException e) {
//            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
//        }
//
//        Long id = commentService.save(request, userEmail);
//
//        return new ResponseEntity(id, HttpStatus.OK);
//    }

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

    @GetMapping("/getCommentList/{gameId}")
    public ResponseEntity<ResponseCommentList> getCommentList(@PathVariable Integer gameId) throws Exception {

        log.info("before retrieve comment data");

        ResponseCommentList response = commentService.getCommentList(Long.valueOf(gameId));

        log.info("added retrieve comment data");

        return new ResponseEntity(response, HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    public ResponseEntity delete(HttpServletRequest httpServletRequest,
                                 @RequestBody @Valid RequestDelete requestDelete,
                                BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            getSubjectInJwt(token);
        } catch (JwtNullTokenException | ExpiredJwtException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        commentService.delete(Long.valueOf(requestDelete.getCommentId()));

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity update(HttpServletRequest httpServletRequest,
                                 @RequestBody @Valid RequestUpdate requestUpdate,
                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            getSubjectInJwt(token);
        } catch (JwtNullTokenException | ExpiredJwtException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        commentService.update(requestUpdate);

        return new ResponseEntity(HttpStatus.OK);
    }
}
