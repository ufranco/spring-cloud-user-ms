package com.ufranco.userservice.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@Getter
@Setter(value = PRIVATE)
@EqualsAndHashCode
@ToString
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String username;

  private String displayname;

  private String email;

  @JsonIgnore
  private String password;

  @Column(insertable = false, updatable = false)
  @CreationTimestamp
  private Date createdAt;

  @Column(insertable = false, updatable = false)
  @UpdateTimestamp
  private Date modifiedAt;


  public User changeId(Long id) {
    return new User(
      id,
      this.username,
      this.displayname,
      this.email,
      this.password,
      this.createdAt,
      this.modifiedAt
    );
  }

  public User changeUsername(String username) {
    return new User(
      this.id,
      username,
      this.displayname,
      this.email,
      this.password,
      this.createdAt,
      this.modifiedAt
    );
  }

  public User changeDisplayname(String displayname) {
    return new User(
      this.id,
      this.username,
      displayname,
      this.email,
      this.password,
      this.createdAt,
      this.modifiedAt
    );
  }

  public User changeEmail(String email) {
    return new User(
      this.id,
      this.username,
      this.displayname,
      email,
      this.password,
      this.createdAt,
      this.modifiedAt
    );
  }

  public User changePassword(String password) {
    return new User(
      this.id,
      this.username,
      this.displayname,
      this.email,
      password,
      this.createdAt,
      this.modifiedAt
    );
  }

  public User changeCreateAt(Date createdAt) {
    return new User(
      this.id,
      this.username,
      this.displayname,
      this.email,
      this.password,
      createdAt,
      this.modifiedAt
    );
  }

  public User changeModifiedAt(Date modifiedAt) {
    return new User(
      this.id,
      this.username,
      this.displayname,
      this.email,
      this.password,
      this.createdAt,
      modifiedAt
    );
  }


}
