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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateUserTest {

  @Mock
  UserRepository repository;

  @InjectMocks
  UserService service;

  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd - HH:mm:ss");

  Date validDate;

  {
    try {
      validDate = dateFormat.parse("2012-05-02 - 16:00:00");
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  User userToBeUpdated = User.builder()
    .id(1L)
    .username("abelain99")
    .displayname("Carlos Santana")
    .email("carlitossantana@gmail.com")
    .password("T3tit@2awqs_2")
    .createdAt(validDate)
    .modifiedAt(validDate)
    .build();


  @AfterEach
  public void resetMocks() {
    reset(repository);
  }

  @Test
  public void validUserTest()  {
    User user = userToBeUpdated.changeDisplayname("Carlit0x Santillan");

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(userToBeUpdated));

    when(repository.existsByUsername(user.getUsername()))
      .thenReturn(false);

    when(repository.existsByEmail(user.getEmail()))
      .thenReturn(false);

    assertDoesNotThrow(() -> service.updateUser(user));


  }

  @Test
  public void nullIdTest() {
    User user = userToBeUpdated
    .changeId(null);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "id",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void notValidIdTest() {
    User user = userToBeUpdated
      .changeId(-1L);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "id",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void IdWithNoUserTest() {
    User user = userToBeUpdated
      .changeId(1L);

    when(repository.existsById(1L)).thenReturn(false);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "id",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void invalidUsernameTest() {
    User user = userToBeUpdated
      .changeUsername("a");

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(userToBeUpdated));


    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "username",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void takenUsernameTest() {
    User user = userToBeUpdated
      .changeUsername("ricardo22")
      .changeEmail("ricardo@gmail.com");

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(userToBeUpdated));

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
    User user = userToBeUpdated
      .changeEmail("carlossantanagmail.com");

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(userToBeUpdated));

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "email",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void takenEmailTest() {
    User user = userToBeUpdated
      .changeEmail("carloto@gmail.com");

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(userToBeUpdated));

    when(repository.existsByUsername(user.getUsername()))
      .thenReturn(false);

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
    User user = userToBeUpdated
      .changePassword(password);

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(userToBeUpdated));

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "password",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void multipleInvalidFieldsTest() {
    User user = userToBeUpdated
      .changeUsername("aa___")
      .changePassword("testito");

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(userToBeUpdated));

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

  @Test
  public void invalidModifiedAtTest() {
    User user = null;
    try {
      user = userToBeUpdated
        .changeModifiedAt(
          dateFormat.parse("2010-05-05 - 00:00:00")
        );
    } catch (ParseException ignored) {}
    assert user != null;

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(userToBeUpdated));

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "modifiedAt",
      exception.getInvalidFields().get(0).getFieldName()
    );

  }

  private InvalidUserFieldsException itThrowsException(User user) {
    return assertThrows(
      InvalidUserFieldsException.class,
      () -> service.updateUser(user)
    );
  }
}

