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




  @AfterEach
  public void resetMocks() {
    reset(repository);
  }

  @Test
  public void validUserTest()  {
    User user = getValidUser();

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(getUserToBeUpdated()));

    when(repository.existsByUsername(user.getUsername()))
      .thenReturn(false);

    when(repository.existsByEmail(user.getEmail()))
      .thenReturn(false);

    assertDoesNotThrow(() -> service.updateUser(user));


  }

  @Test
  public void nullIdTest() {
    User user = getValidUser();
    user.setId(null);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "id",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void notValidIdTest() {
    User user = getValidUser();
    user.setId(-1L);

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "id",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void IdWithNoUserTest() {
    User user = getValidUser();
    user.setId(1L);

    when(repository.existsById(1L)).thenReturn(false);

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

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(getUserToBeUpdated()));


    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "username",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void takenUsernameTest() {
    User user = getValidUser();

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(getUserToBeUpdated()));

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

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(getUserToBeUpdated()));

    InvalidUserFieldsException exception = itThrowsException(user);

    assertEquals(
      "email",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void takenEmailTest() {
    User user = getValidUser();
    user.setEmail("carloto@gmail.com");

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(getUserToBeUpdated()));

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

  @Test
  public void invalidPasswordTest() {
    User user = getValidUser();
    user.setPassword("t3stit0_");

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(getUserToBeUpdated()));

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

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(getUserToBeUpdated()));

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
    User user = getValidUser();

    try {
      user.setModifiedAt(
        dateFormat.parse("2010-05-05 - 00:00:00")
      );

    } catch (ParseException ignored) {}

    when(repository.existsById(user.getId()))
      .thenReturn(true);

    when(repository.findById(user.getId()))
      .thenReturn(Optional.of(getUserToBeUpdated()));

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

  private User getValidUser() {
    return new User(
      1L,
      "abelain99",
      "Carlos Santana",
      "carlitossantana@gmail.com",
      "T3tit@2aw2",
      validDate,
      validDate
    );
  }

  private User getUserToBeUpdated() {
    return new User(
      1L,
      "abelain2009",
      "Carlos Santana",
      "carlitossantana@gmail.com",
      "T3tit@2awqs_2",
      validDate,
      validDate
    );
  }

}

