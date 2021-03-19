package com.ufranco.userservice.services;

import com.ufranco.userservice.models.exceptions.InvalidUserFieldsException;
import com.ufranco.userservice.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteUserTest {

  @Mock
  UserRepository repository;

  @InjectMocks
  UserService service;

  @AfterEach
  public void resetMocks() {
    reset(repository);
  }

  @Test
  public void validUserIdTest() {
    Long id = 1L;

    when(repository.existsById(id))
      .thenReturn(true);

    assertDoesNotThrow(() -> service.deleteUser(id));
  }

  @Test
  public void negativeUserIdTest() {
    Long id = -1L;

    assertThrows(
      InvalidUserFieldsException.class,
      () -> service.deleteUser(id)
    );
  }

  @Test
  public void idWithNoUserUserTest() {
    Long id = 1L;

    when(repository.existsById(id))
      .thenReturn(false);

    assertThrows(
      InvalidUserFieldsException.class,
      () -> service.deleteUser(id)
    );
  }

}
