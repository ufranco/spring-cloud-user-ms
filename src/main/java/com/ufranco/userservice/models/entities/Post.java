package com.ufranco.userservice.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
  private Long id;
  private Long userId;
  private String text;
  private Date createdAt;
  private Date modifiedAt;
}

