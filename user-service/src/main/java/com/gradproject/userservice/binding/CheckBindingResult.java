package com.gradproject.userservice.binding;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckBindingResult {

    public static ResponseEntity induceSuccessInAjax(BindingResult bindingResult) {

        Map<String, ErrorDetail> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
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

        Map<String, ErrorDetail> errorMap = new HashMap<>();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                ErrorDetail errorDetail = new ErrorDetail(fieldError.getRejectedValue(), fieldError.getDefaultMessage());
                errorMap.put(fieldError.getField(), errorDetail);
            }

            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    null,
                    errorMap
            ), HttpStatus.BAD_REQUEST);
    }
}
