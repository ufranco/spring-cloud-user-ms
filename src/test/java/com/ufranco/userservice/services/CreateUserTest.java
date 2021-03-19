package com.ufranco.userservice.services;

import com.ufranco.userservice.models.entities.User;
import com.ufranco.userservice.models.exceptions.InvalidUserFieldsException;
import com.ufranco.userservice.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

  User validUser = User.builder()
    .id(null)
    .username("abelain99")
    .displayname("Carlos Santana")
    .email("carlitossantana@gmail.com")
    .password("T3tit@2aw2")
    .createdAt(null)
    .modifiedAt(null)
    .build();

  @AfterEach
  public void resetMocks() {
    reset(repository);
  }

  @Test
  public void validUserTest()  {
    User user = validUser;

    when(repository.existsByUsername(user.getUsername()))
      .thenReturn(false);

    when(repository.existsByEmail(user.getEmail()))
      .thenReturn(false);

    assertDoesNotThrow(() -> service.createUser(user));

  }

  @Test
  public void parameterizedIdTest() {
    User user = validUser.changeId(1L);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "id",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void nullUsernameTest() {
    User user = validUser
      .changeUsername(null);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "username",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void invalidUsernameTest() {
    User user = validUser
      .changeUsername("a");

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "username",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void nullDisplaynameTest() {
    User user = validUser
      .changeDisplayname(null);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "displayname",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void displaynameTooLargeTest() {
    User user = validUser
      .changeDisplayname("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "displayname",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void takenUsernameTest() {
    User user = validUser;

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
    User user = validUser
      .changeEmail("carlossantanagmail.com");

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "email",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void takenEmailTest() {
    User user = validUser;
    when(repository.existsByEmail(user.getEmail()))
      .thenReturn(true);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "email",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "",
    "a",
    "testarudo",
    "TESTARUDO",
    "testarudo9",
    "Testarudo9",
    "T3starudo9",
    "T3sta rudo9"
  })
  public void invalidPasswordTest(String password) {
    User user = validUser
      .changePassword(password);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "password",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void multipleInvalidFieldsTest() {
      User user = validUser
        .changeUsername("aa___")
        .changePassword("testito");

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

}
