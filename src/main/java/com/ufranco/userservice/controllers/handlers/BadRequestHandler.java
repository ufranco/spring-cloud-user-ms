package com.ufranco.userservice.controllers.handlers;

import com.ufranco.userservice.models.exceptions.InvalidQueryParametersException;
import com.ufranco.userservice.models.exceptions.InvalidUserFieldsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class BadRequestHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InvalidQueryParametersException.class)
  public ResponseEntity<Object> invalidQueryFieldsError(
    InvalidQueryParametersException exception,
    WebRequest request
  ) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", exception.getMessage());
    body.put("invalidFields", exception.getInvalidFields());
    return new ResponseEntity<>(body, BAD_REQUEST);
  }

  @ExceptionHandler(InvalidUserFieldsException.class)
  public ResponseEntity<Object> invalidUserFieldsError(
    InvalidUserFieldsException exception,
    WebRequest request
  ) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("message", exception.getMessage());
    body.put("invalidFields", exception.getInvalidFields());
    return new ResponseEntity<>(body, BAD_REQUEST);
  }



}
