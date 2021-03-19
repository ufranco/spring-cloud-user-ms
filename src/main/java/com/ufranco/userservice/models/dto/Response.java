package com.ufranco.userservice.models.dto;

import com.ufranco.userservice.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
  private User user;
  private List<String> messages;

}
