package com.ufranco.userservice.models.entities;

import lombok.*;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter(value = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class Post {
  private Long id;
  private Long userId;
  private String text;
  private Date createdAt;
  private Date modifiedAt;
}

