package com.ufranco.userservice.models.entities;

import lombok.*;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@Getter
@Setter(value = PRIVATE)
@EqualsAndHashCode
@ToString
public class Post {
  private Long id;
  private Long userId;
  private String text;
  private Date createdAt;
  private Date modifiedAt;
}

