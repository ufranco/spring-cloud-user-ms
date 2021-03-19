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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUserTest {

  @Mock
  UserRepository repository;

  @InjectMocks
  UserService service;

  @AfterEach
  public void resetMocks() {
    reset(repository);
  }

  @Test
  public void validRequest() {
    Long id = 1L;

    when(repository.existsById(id))
      .thenReturn(true);

    when(repository.findById(id))
      .thenReturn(Optional.of(new User()));

    assertDoesNotThrow(() -> service.getUser(id));
  }

  @Test
  public void IdWithNoUserRequest() {
    Long id = 1L;

    when(repository.existsById(id))
      .thenReturn(false);

    InvalidUserFieldsException exception = itThrowsException(id);

    assertEquals(
      "id",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }


  @Test
  public void negativeIdRequest() {
    Long id = -1L;

    InvalidUserFieldsException exception = itThrowsException(id);

    assertEquals(
      "id",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }


  private InvalidUserFieldsException itThrowsException(Long id) {
    return assertThrows(
      InvalidUserFieldsException.class,
      () -> service.getUser(id)
    );
  }
}
