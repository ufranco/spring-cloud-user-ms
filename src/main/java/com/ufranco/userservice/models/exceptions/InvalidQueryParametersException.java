package com.ufranco.userservice.models.exceptions;

import com.ufranco.userservice.models.exceptions.abstracts.InvalidFieldsException;

import java.util.List;

public class InvalidQueryParametersException extends InvalidFieldsException {

  public InvalidQueryParametersException(List<InvalidField> invalidFields) {
    super("Invalid query parameters.", invalidFields);
  }
}
