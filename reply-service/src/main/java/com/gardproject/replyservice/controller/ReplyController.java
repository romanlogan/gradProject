package com.gardproject.replyservice.controller;

import com.gardproject.replyservice.binding.CheckBindingResult;
import com.gardproject.replyservice.dto.RequestDelete;
import com.gardproject.replyservice.dto.RequestSave;
import com.gardproject.replyservice.dto.RequestUpdate;
import com.gardproject.replyservice.dto.ResponseReplies;
import com.gardproject.replyservice.exception.JwtNullTokenException;
import com.gardproject.replyservice.service.ReplyService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ReactiveTypeDescriptor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class ReplyController {

    private final ReplyService replyService;

    private final Environment env;

    @Autowired
    public ReplyController(ReplyService replyService, Environment env) {
        this.replyService = replyService;
        this.env = env;
    }

    @PostMapping("/save")
    public ResponseEntity save(HttpServletRequest httpServletRequest,
                               @RequestBody @Valid RequestSave requestSave,
                               BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        String userEmail = httpServletRequest.getHeader("X-User-Email");

        requestSave.setCreatedAt(LocalDateTime.now());
        Long id = replyService.save(requestSave, userEmail);

        return new ResponseEntity(id, HttpStatus.OK);
    }

    @GetMapping("/replies/{commentId}")
    public ResponseEntity<ResponseReplies> getReplies(@PathVariable Integer commentId) {

        ResponseReplies response = replyService.getReplies(Long.valueOf(commentId));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(HttpServletRequest httpServletRequest,
                                 @RequestBody @Valid RequestDelete requestDelete,
                                    BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        replyService.delete(requestDelete);

        return new ResponseEntity(HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity update(HttpServletRequest httpServletRequest,
                                 @RequestBody @Valid RequestUpdate requestUpdate,
                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        replyService.update(requestUpdate);

        return new ResponseEntity(HttpStatus.OK);
    }
}

