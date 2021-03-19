package com.ufranco.userservice.models.dto;

public record QueryParams(
  String name,
  String sortBy,
  String order,
  Integer page,
  Integer limit
) {}
