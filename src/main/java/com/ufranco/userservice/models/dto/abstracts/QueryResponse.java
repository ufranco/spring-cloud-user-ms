package com.ufranco.userservice.models.dto.abstracts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class QueryResponse {
  private Integer records;
  private List<String> messages;


}
