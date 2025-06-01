package com.gradproject.commentservice.binding;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ApiResponse<T> {

    private int code;   //status code
    private HttpStatus status;
    private List<T> dataList;
    private Map<String, ErrorDetail> errorMap = new HashMap<>();



    public ApiResponse(HttpStatus status, List<T> dataList, Map<String, ErrorDetail> errorMap ) {
        this.code = status.value();
        this.status = status;
        this.errorMap = errorMap;
        this.dataList = dataList;
    }

    public static <T> ApiResponse<T> of(HttpStatus status,  List<T> dataList, Map<String, ErrorDetail> errorMap ) {

        return new ApiResponse<>(status,dataList,errorMap);
    }

//    public static <T> ApiResponse<T> of(HttpStatus status, T data) {
//
//        return new ApiResponse<>(status, status.name(), data);
//    }

//    public static <T> ApiResponse<T> ok(T data) {
//
//        return new ApiResponse<>( HttpStatus.OK, HttpStatus.OK.name(), data);
//    }
}
