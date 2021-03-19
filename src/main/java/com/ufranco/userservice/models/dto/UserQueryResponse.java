package com.ufranco.userservice.models.dto;

import com.ufranco.userservice.models.dto.abstracts.QueryResponse;
import com.ufranco.userservice.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryResponse extends QueryResponse {
  List<User> users;

  public UserQueryResponse(List<User> users, Integer records, List<String> messages) {
    super(records, messages);
    this.users = users;
  }
}
