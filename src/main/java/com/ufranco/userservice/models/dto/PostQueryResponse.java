package com.ufranco.userservice.models.dto;

import com.ufranco.userservice.models.dto.abstracts.QueryResponse;
import com.ufranco.userservice.models.entities.Post;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
@Builder
public class PostQueryResponse extends QueryResponse {
  List<Post> posts;
}



