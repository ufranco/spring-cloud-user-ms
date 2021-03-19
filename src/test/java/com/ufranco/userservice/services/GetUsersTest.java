package com.ufranco.userservice.services;

import com.ufranco.userservice.models.dto.QueryParams;
import com.ufranco.userservice.models.entities.User;
import com.ufranco.userservice.models.exceptions.InvalidQueryParametersException;
import com.ufranco.userservice.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GetUsersTest {

  @Mock(answer = RETURNS_DEEP_STUBS)
  UserRepository repository;

  @InjectMocks
  UserService service;

  QueryParams validQueryParams = QueryParams.builder()
    .name("carlo")
    .sortBy("username")
    .order("asc")
    .page(0)
    .limit(10)
    .build();

  @AfterEach
  public void resetMocks() {
    reset(repository);
  }

  @ParameterizedTest
  @ValueSource(strings = {
    "",
    "carlo"
  })
  public void validRequestTest(String name) {

    when(repository.findAll(
      (Pageable) Mockito.any()
      )
      .getContent()
    ).thenReturn(
      List.of(
        User.builder()
          .build()
      )
    );

    QueryParams params = validQueryParams.changeName(name);
    assertDoesNotThrow(() -> service.getUsers(params));
  }

  @Test
  public void maliciousSortByFieldTest() {
    QueryParams params = validQueryParams
      .changeSortBy("password");

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "sortBy",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void invalidSortByFieldTest() {
    QueryParams params = validQueryParams
      .changeSortBy("asfagaga");

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "sortBy",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }
  @Test
  public void invalidOrderFieldTest() {
    QueryParams params = validQueryParams
      .changeOrder("dasc");

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "order",
      exception.getInvalidFields().get(0).getFieldName()
    );

  }

  @Test
  public void invalidPageFieldTest() {
    QueryParams params = validQueryParams
      .changePage(-1);

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "page",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void invalidLimitFieldTest() {
    QueryParams params = validQueryParams
      .changeLimit(-1);

    InvalidQueryParametersException exception = itThrowsException(params);

    assertEquals(
      "limit",
      exception.getInvalidFields().get(0).getFieldName()
    );
  }

  @Test
  public void limitTooLargeFieldTest() {
    QueryParams params = validQueryParams
      .changeLimit(150);

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
