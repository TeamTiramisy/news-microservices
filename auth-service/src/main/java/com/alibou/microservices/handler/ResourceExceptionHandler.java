package com.alibou.microservices.handler;


import com.alibou.microservices.dto.ErrorDto;
import com.alibou.microservices.exception.PasswordException;
import com.alibou.microservices.util.Constant;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorDto> notValid(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorDto.builder()
                                .message(exception.getMessage())
                                .errorCode(Constant.ERROR_CODE_VALID)
                                .build());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDto> handle(FeignException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorDto.builder()
                        .message(exception.getMessage())
                        .errorCode(Constant.ERROR_CODE)
                        .build());
    }

    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<ErrorDto> notValid(PasswordException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ErrorDto.builder()
                                .message(exception.getMessage())
                                .errorCode(Constant.ERROR_CODE_VALID)
                                .build());
    }
}
