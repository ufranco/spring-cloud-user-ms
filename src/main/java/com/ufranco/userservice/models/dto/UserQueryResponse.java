package com.ufranco.userservice.models.dto;

import com.ufranco.userservice.models.dto.abstracts.QueryResponse;
import com.ufranco.userservice.models.entities.User;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
public class UserQueryResponse extends QueryResponse {
  List<User> users;

  public UserQueryResponse(List<User> users, Integer records, List<String> messages) {
    super(records, messages);
    this.users = users;
  }
}
