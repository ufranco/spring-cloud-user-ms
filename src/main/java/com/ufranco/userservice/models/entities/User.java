package com.ufranco.userservice.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}
