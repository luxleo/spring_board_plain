package com.luxlog.api.controller;

import com.luxlog.api.exception.LuxLogException;
import com.luxlog.api.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PostControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        //ErrorResponse response = new ErrorResponse("400", "잘못된 요청입니다");
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .errorMessage("잘못된 요청이오")
                .build();

        FieldError fieldError = e.getFieldError();
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();

        response.addValidation(field, message);
        return response;
    }

    @ResponseBody
    @ExceptionHandler(LuxLogException.class)
    public ResponseEntity<ErrorResponse> noSuchPostHandler(LuxLogException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(e.getStatus()))
                .errorMessage(e.getMessage())
                .validation(e.getValidations())
                .build();

        return ResponseEntity.status(Integer.parseInt(errorResponse.getCode()))
                .body(errorResponse);
    }

// Old version
//    public Map<String, String> invalidRequestHandler(MethodArgumentNotValidException e) {
//        FieldError fieldError = e.getFieldError();
//        String field = fieldError.getField();
//        String message = fieldError.getDefaultMessage();
//
//        Map<String, String> response = new HashMap<>();
//        response.put(field, message);
//        return response;
//    }

}
