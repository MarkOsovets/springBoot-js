package simpleapp.demo.web;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log= LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(
        Exception e
    ){
        log.error("Handle exception", e);

        var errroDto= new ErrorResponseDto(
        "Internal server Error",
        e.getMessage(),
        LocalDateTime.now()
         );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errroDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(
        EntityNotFoundException e
    ){
        log.error("Handle entityNotFoundException", e);

        var errroDto= new ErrorResponseDto(
        "Entity not found",
        e.getMessage(),
        LocalDateTime.now()
         );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errroDto);
    }

    // @ExceptionHandler(exception = {IllegalArgumentException.class,
    //      IllegalStateException.class})
    //     public ResponseEntity<String> handleBadRequest(
    //         IllegalArgumentException e
    //     ){
    //         log.error("Handle IllegalArgumentException", e);

    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    //     }
         

    @ExceptionHandler({
    IllegalArgumentException.class,
    IllegalStateException.class,
    MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponseDto> handleBadRequest(Exception e) {
        log.error("Handle bad request exception", e);

        var errroDto= new ErrorResponseDto(
        "Bad Request",
        e.getMessage(),
        LocalDateTime.now()
         );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errroDto);
    }
}
