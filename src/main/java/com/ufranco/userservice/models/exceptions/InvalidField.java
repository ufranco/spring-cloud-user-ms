package com.ufranco.userservice.models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvalidField {
  private String fieldName;
  private List<String> messages;
}
