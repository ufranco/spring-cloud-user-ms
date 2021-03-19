package com.ufranco.userservice.services;

import com.ufranco.userservice.models.entities.User;
import com.ufranco.userservice.models.exceptions.InvalidUserFieldsException;
import com.ufranco.userservice.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateUserTest {

  @Mock
  UserRepository repository;

  @InjectMocks
  UserService service;

  @AfterEach
  public void resetMocks() {
    reset(repository);
  }

  @Test
  public void validUserTest()  {
    User user = getValidUser();

    when(repository.existsByUsername(user.getUsername()))
      .thenReturn(false);

    when(repository.existsByEmail(user.getEmail()))
      .thenReturn(false);

    assertDoesNotThrow(() -> service.createUser(user));

  }

  @Test
  public void parameterizedIdTest() {
    User user = getValidUser();
    user.setId(1L);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "id",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void invalidUsernameTest() {
    User user = getValidUser();
    user.setUsername("a");

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "username",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void takenUsernameTest() {
    User user = getValidUser();

    when(repository.existsByUsername(user.getUsername()))
      .thenReturn(true);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "username",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }


  @Test
  public void invalidEmailTest() {
    User user = getValidUser();
    user.setEmail("carlossantanagmail.com");

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "email",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void takenEmailTest() {
    User user = getValidUser();
    when(repository.existsByEmail(user.getEmail()))
      .thenReturn(true);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "email",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void invalidPasswordTest() {
    User user = getValidUser();
    user.setPassword("t3stit0_");

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "password",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void multipleInvalidFieldsTest() {
      User user = getValidUser();
      user.setUsername("aa___");
      user.setPassword("testito");

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "username",
      exception.getInvalidFields().get(0).getFieldName()
    );

    assertEquals(
      "password",
      exception.getInvalidFields().get(1).getFieldName()

    );
  }

  private InvalidUserFieldsException itThrowsException(User user) {
    return assertThrows(
      InvalidUserFieldsException.class,
      () -> service.createUser(user)
    );
  }

  private User getValidUser() {
    return new User(
      null,
      "abelain99",
      "Carlos Santana",
      "carlitossantana@gmail.com",
      "T3tit@2aw2",
      null,
      null
    );
  }

}
