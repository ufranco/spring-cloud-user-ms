package com.ufranco.userservice.models.dto;

import lombok.*;


@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class QueryParams {

  private final String name;
  private final String  sortBy;
  private final String order;
  private final Integer page;
  private final Integer limit;

  public QueryParams changeName(String name) {
    return new QueryParams(
      name,
      this.sortBy,
      this.order,
      this.page,
      this.limit
    );
  }

  public QueryParams changeSortBy(String sortBy) {
    return new QueryParams(
      this.name,
      sortBy,
      this.order,
      this.page,
      this.limit
    );
  }

  public QueryParams changeOrder(String order) {
    return new QueryParams(
      this.name,
      this.sortBy,
      order,
      this.page,
      this.limit
    );
  }

  public QueryParams changePage(Integer page) {
    return new QueryParams(
      this.name,
      this.sortBy,
      this.order,
      page,
      this.limit
    );
  }
    public QueryParams changeLimit(Integer limit) {
    return new QueryParams(
      this.name,
      this.sortBy,
      this.order,
      this.page,
      limit
    );

  }


}
