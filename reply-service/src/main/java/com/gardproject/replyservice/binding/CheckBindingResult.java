package com.gardproject.replyservice.binding;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckBindingResult {

//    유저의 입력이 잘못되었을떄 Ajax 의 success 로 유도해서 유저에게 잘못된 값을 다시 보여주는 용도
    public static ResponseEntity induceSuccessInAjax(BindingResult bindingResult) {

        Map<String, ErrorDetail> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {         //들어가는 순서는 랜덤

                ErrorDetail errorDetail = new ErrorDetail(fieldError.getRejectedValue(), fieldError.getDefaultMessage());
                errorMap.put(fieldError.getField(), errorDetail);
            }

            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    null,
                    errorMap
            ), HttpStatus.OK);
    }


    public static ResponseEntity induceErrorInAjax(BindingResult bindingResult) {

//            List<String> messageList = new ArrayList<>();
//            List<Object> dataList = new ArrayList<>();
        Map<String, ErrorDetail> errorMap = new HashMap<>();


        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
//                dataList.add(fieldError.getRejectedValue());
//                messageList.add(fieldError.getDefaultMessage());
                ErrorDetail errorDetail = new ErrorDetail(fieldError.getRejectedValue(), fieldError.getDefaultMessage());

                errorMap.put(fieldError.getField(), errorDetail);
            }

//          오류 데이터를 다시 보낼 필요가 없으므로 success 로 유도할 필요 없으므로 BAD_REQUEST
            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    null,
                    errorMap
            ), HttpStatus.BAD_REQUEST);
    }
}
