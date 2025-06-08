package com.time.timemanager.config;

import com.time.timemanager.authentication.exceptions.AccountInactiveException;
import com.time.timemanager.authentication.exceptions.AccountUnconfirmedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<?> handleEnumBindingError(HttpMessageConversionException ex) {
        return ResponseEntity.badRequest().body(ApiResponseMapper.errorResponse("Invalid value for enum field: " + ex.getMessage() + "."));
    }

    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<?> handleAccountInactiveException(AccountInactiveException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseMapper.errorResponse(ex.getMessage() + "."));
    }

    @ExceptionHandler(AccountUnconfirmedException.class)
    public ResponseEntity<?> handleAccountUnconfirmedException(AccountUnconfirmedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponseMapper.errorResponse(ex.getMessage() + "."));
    }
}
