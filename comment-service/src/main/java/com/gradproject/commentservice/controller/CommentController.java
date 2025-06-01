package com.gradproject.commentservice.controller;

import com.gradproject.commentservice.binding.CheckBindingResult;
import com.gradproject.commentservice.dto.RequestDelete;
import com.gradproject.commentservice.dto.RequestSaveComment;
import com.gradproject.commentservice.dto.RequestUpdate;
import com.gradproject.commentservice.dto.ResponseCommentList;
import com.gradproject.commentservice.exception.JwtNullTokenException;
import com.gradproject.commentservice.exception.UserCommentAlreadyExistException;
import com.gradproject.commentservice.service.CommentService;
//import com.gradproject.commentservice.service.KafkaProducer;
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

//    private KafkaProducer kafkaProducer;

    @Autowired
    public CommentController(CommentService commentService,
                             Environment env
//                             KafkaProducer kafkaProducer
                             ) {
        this.commentService = commentService;
        this.env = env;
//        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/save")
    public ResponseEntity save(HttpServletRequest httpServletRequest,
                               @RequestBody @Valid RequestSaveComment request,
                               BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        String userEmail = httpServletRequest.getHeader("X-User-Email");

        Long id = null;

        try {
//            blocking I/O
            id = commentService.save(request, userEmail);


//            non-blocking I/O
//            kafkaProducer.send("saveComment-topic",request);

        } catch (UserCommentAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return new ResponseEntity(id, HttpStatus.OK);
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

        commentService.update(requestUpdate);

        return new ResponseEntity(HttpStatus.OK);
    }
}
