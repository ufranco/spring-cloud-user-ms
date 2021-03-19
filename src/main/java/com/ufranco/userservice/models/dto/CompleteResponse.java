package com.ufranco.userservice.models.dto;

import com.ufranco.userservice.models.entities.Post;
import com.ufranco.userservice.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
public class CompleteResponse {
  private final User user;
  private final List<Post> posts;
  private final List<String> messages;
}
