package com.ufranco.userservice.models.dto;

import com.ufranco.userservice.models.dto.abstracts.QueryResponse;
import com.ufranco.userservice.models.entities.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PostQueryResponse extends QueryResponse {
  List<Post> posts;
}
