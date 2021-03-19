package com.ufranco.userservice.models.exceptions;

import com.ufranco.userservice.models.exceptions.abstracts.InvalidFieldsException;

import java.util.List;

public class InvalidUserFieldsException extends InvalidFieldsException {

  public InvalidUserFieldsException(List<InvalidField> invalidFields) {
    super("Invalid user fields.", invalidFields);
  }
}
