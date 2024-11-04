package com.gradproject.userservice.repository;


import com.gradproject.userservice.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<UserEntity, Long> {


    UserEntity findByUserId(String userId);

    UserEntity findByEmail(String email);



}
