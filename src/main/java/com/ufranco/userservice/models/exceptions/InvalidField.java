package com.ufranco.userservice.models.exceptions;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
@Builder
public class InvalidField {
  private final String fieldName;
  private final List<String> messages;
}
