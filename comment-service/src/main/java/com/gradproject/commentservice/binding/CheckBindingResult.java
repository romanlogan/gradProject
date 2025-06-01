package com.gradproject.commentservice.binding;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckBindingResult {

    // When the user's input is incorrect, it is used to show the incorrect value to the user again by inducing Ajax success logic.
    public static ResponseEntity induceSuccessInAjax(BindingResult bindingResult) {

        Map<String, ErrorDetail> errorMap = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {         // The order of entry is random

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

// Since there is no need to send error data again, there is no need to lead to success, so return BAD_REQUEST
            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    null,
                    errorMap
            ), HttpStatus.BAD_REQUEST);
    }
}
