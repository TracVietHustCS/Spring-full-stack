package org.project1.shopweb.exception;

import lombok.RequiredArgsConstructor;
import org.project1.shopweb.component.LocalizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor

@ControllerAdvice
public class ExceptionHandler {
    @Autowired
    LocalizationUtils localizationUtils;



//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errors);
    }

//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorRespon> handleNotFoundException(NotFoundException exception) {
        ErrorRespon errorRespon = ErrorRespon.builder()
                .message(exception.getMessage())
                .time(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorRespon);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespon> handleException(Exception exception) {
        ErrorRespon errorRespon = ErrorRespon.builder()
                .message(exception.getMessage())
                .time(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorRespon);
    }

    //the better paratice is you can use enum(code,message) then use that to implement i18n
    // demo

    @org.springframework.web.bind.annotation.ExceptionHandler(DemoI18nException.class)
    public ResponseEntity<ErrorRespon> hanleCustomException(DemoI18nException ex){
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorRespon.builder()
                        .time(LocalDateTime.now())
                        .status(errorCode.getCode())
                        .message(localizationUtils.getLocalizationMessages(errorCode.getMessageKey(),ex.getArgs())) // dev/test eviroment
                        .build());



    }






}
