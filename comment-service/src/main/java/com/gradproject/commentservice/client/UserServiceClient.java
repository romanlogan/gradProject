package com.gradproject.commentservice.client;


import com.gradproject.commentservice.dto.RequestUser;
import com.gradproject.commentservice.dto.ResponseUser;
import com.gradproject.commentservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {


    @ResponseBody
    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user);
}
