package br.com.app.transaction.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHandler extends RuntimeException {

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<Object> handleInvalidTransactionException(InvalidTransactionException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}