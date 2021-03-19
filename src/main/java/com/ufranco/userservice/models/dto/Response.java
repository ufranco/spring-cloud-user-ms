package com.ufranco.userservice.models.dto;

import com.ufranco.userservice.models.entities.User;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class Response {
  private User user;
  private List<String> messages;

}
