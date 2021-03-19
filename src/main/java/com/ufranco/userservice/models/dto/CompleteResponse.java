package com.ufranco.userservice.models.dto;

import com.ufranco.userservice.models.entities.Post;
import com.ufranco.userservice.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteResponse {
  private User user;
  private List<Post> posts;
  private List<String> messages;
}
