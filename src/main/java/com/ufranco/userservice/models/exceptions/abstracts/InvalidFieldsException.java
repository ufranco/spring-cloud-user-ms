package com.ufranco.userservice.models.exceptions.abstracts;

import com.ufranco.userservice.models.exceptions.InvalidField;

import java.util.List;


public abstract class InvalidFieldsException extends Exception {
  private final List<InvalidField> invalidFields;

  public InvalidFieldsException(String message, List<InvalidField> invalidFields) {
    super(message);
    this.invalidFields = invalidFields;
  }

  public List<InvalidField> getInvalidFields() {
    return invalidFields;
  }
}
