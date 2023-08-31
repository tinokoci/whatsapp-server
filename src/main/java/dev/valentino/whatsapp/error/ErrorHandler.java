package dev.valentino.whatsapp.error;

import dev.valentino.whatsapp.chat.exception.ChatActionAccessException;
import dev.valentino.whatsapp.chat.exception.ChatException;
import dev.valentino.whatsapp.message.exception.MessageException;
import dev.valentino.whatsapp.user.exception.UserException;
import dev.valentino.whatsapp.user.exception.UserFieldInUseException;
import dev.valentino.whatsapp.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ChatActionAccessException.class)
    public ResponseEntity<ErrorInfo> userExceptionHandler(ChatActionAccessException exception, WebRequest request) {
        ErrorInfo error = new ErrorInfo(exception.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorInfo> userExceptionHandler(ChatException exception, WebRequest request) {
        ErrorInfo error = new ErrorInfo(exception.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserFieldInUseException.class)
    public ResponseEntity<ErrorInfo> userExceptionHandler(UserFieldInUseException exception, WebRequest request) {
        ErrorInfo error = new ErrorInfo(exception.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorInfo> userExceptionHandler(UserNotFoundException exception, WebRequest request) {
        ErrorInfo error = new ErrorInfo(exception.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorInfo> userExceptionHandler(MessageException exception, WebRequest request) {
        ErrorInfo error = new ErrorInfo(exception.getMessage(), request.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
