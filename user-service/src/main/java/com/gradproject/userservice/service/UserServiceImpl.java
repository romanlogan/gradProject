package com.gradproject.userservice.service;

import com.gradproject.userservice.client.HistoryServiceClient;
import com.gradproject.userservice.dto.*;

import com.gradproject.userservice.entity.UserEntity;
import com.gradproject.userservice.exception.DuplicateEmailException;
import com.gradproject.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment env;
    RestTemplate restTemplate;
    HistoryServiceClient historyServiceClient;
    CircuitBreakerFactory circuitBreakerFactory;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           Environment environment,
                           HistoryServiceClient historyServiceClient,
                           CircuitBreakerFactory circuitBreakerFactory,
                           RestTemplate restTemplate){

//                           OrderServiceClient orderServiceClient) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = environment;
        this.restTemplate = restTemplate;
        this.historyServiceClient = historyServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
//        this.orderServiceClient = orderServiceClient;
    }



    @Override
    public Long createUser(UserDto userDto) {

        checkDuplicateEmailRegistration(userDto);

        userDto.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = UserEntity.create(userDto, passwordEncoder);


        userRepository.save(userEntity);

        return userEntity.getId();
    }

    private void checkDuplicateEmailRegistration(UserDto userDto) {
        UserEntity existedUser = userRepository.findByEmail(userDto.getEmail());

        if (existedUser != null) {
            throw new DuplicateEmailException("This email address is already registered");
        }
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true,
                true,
                true,
                true,
                new ArrayList<>());  //로그인 된 후 할 수 있는 권한을 추가
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null)
            throw new UsernameNotFoundException(email);

//        ModelMapper mapper = new ModelMapper ();
//        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public ResponseMyInfo getResponseMyInfo(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        checkUserExist(email, userEntity);

        log.info("before call history service");
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");

        ResponseEntity<ResponseHistory> historyResponse =
                circuitbreaker.run(() -> historyServiceClient.getPlayedGameList(email),
                throwable -> ResponseEntity.ok(ResponseHistory.createEmpty()));

        log.info("after call history service");

        ResponseHistory history = historyResponse.getBody();

        return ResponseMyInfo.create(userEntity, history);

    }

    private static void checkUserExist(String email, UserEntity userEntity) {
        if (userEntity == null)
            throw new UsernameNotFoundException(email);
    }
}
