package com.ufranco.userservice.services;

import com.ufranco.userservice.models.dto.QueryParams;
import com.ufranco.userservice.models.exceptions.InvalidQueryParametersException;
import com.ufranco.userservice.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;


@ExtendWith(MockitoExtension.class)
public class GetUsersTest {

  @Mock
  UserRepository repository;

  @InjectMocks
  UserService service;

  @AfterEach
  public void resetMocks() {
    reset(repository);
  }

  @Test
  public void validRequestTest() {
    QueryParams params = new QueryParams(
      "pewdie",
      "username",
      "asc",
      0,
      10
    );

    assertDoesNotThrow(() -> service.getUsers(params));

  }

  @Test
  public void maliciousSortByFieldTest() {
    QueryParams params = new QueryParams(
      "pewdie",
      "password",
      "asc",
      0,
      10
    );

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "sortBy",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void invalidSortByFieldTest() {
    QueryParams params = new QueryParams(
      "pewdie",
      "-",
      "asc",
      0,
      10
    );

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "sortBy",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }
  @Test
  public void invalidOrderFieldTest() {
    QueryParams params = new QueryParams(
      "pewdie",
      "username",
      "dasc",
      0,
      10
    );

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "order",
      exception.getInvalidFields().get(0).getFieldName()
    );

  }

  @Test
  public void invalidPageFieldTest() {
    QueryParams params = new QueryParams(
      "pewdie",
      "username",
      "asc",
      -1,
      10
    );

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "page",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void invalidLimitFieldTest() {
    QueryParams params = new QueryParams(
      "pewdie",
      "sfsafaga",
      "asc",
      0,
      0
    );

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "limit",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void limitTooLargeFieldTest() {
    QueryParams params = new QueryParams(
      "pewdie",
      "sfsafaga",
      "asc",
      0,
      101
    );

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "limit",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }


  private InvalidQueryParametersException itThrowsException(QueryParams params) {
    return assertThrows(
      InvalidQueryParametersException.class,
      () -> service.getUsers(params)
    );
  }

}
