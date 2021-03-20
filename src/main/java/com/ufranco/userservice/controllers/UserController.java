package com.ufranco.userservice.controllers;

import com.ufranco.userservice.models.dto.CompleteResponse;
import com.ufranco.userservice.models.dto.QueryParams;
import com.ufranco.userservice.models.dto.Response;
import com.ufranco.userservice.models.dto.UserQueryResponse;
import com.ufranco.userservice.models.entities.User;
import com.ufranco.userservice.models.exceptions.abstracts.InvalidFieldsException;
import com.ufranco.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

  @Autowired
  UserService service;

  @Autowired
  WebClient.Builder webClient;

  @GetMapping
  public UserQueryResponse getUsers(
    @RequestParam(defaultValue = "") String name,
    @RequestParam(defaultValue = "username") String sortBy,
    @RequestParam(defaultValue = "asc") String order,
    @RequestParam(name = "page", defaultValue = "0") Integer page,
    @RequestParam(name = "limit", defaultValue = "10") Integer limit
  ) throws InvalidFieldsException {
    QueryParams params = new QueryParams(
      name,
      sortBy,
      order,
      page,
      limit
    );

    return service.getUsers(params);
  }

  @GetMapping(path = "/{id}")
  public CompleteResponse getUser(@PathVariable("id") Long id) throws InvalidFieldsException {
    Response userResponse = service.getUser(id);

    /*
    PostQueryResponse postResponse =
      webClient.build()
        .get()
        .uri("http://post-service/post/user/" + id)
        .retrieve()
        .bodyToMono(PostQueryResponse.class)
        .block();

    assert postResponse != null;
    */

    return new CompleteResponse(
        userResponse.getUser(),
        null, //postResponse.getPosts(),
        userResponse.getMessages() //userPostsDeletion.messages()
    );
  }

  @PostMapping
  public Response createUser(
    @NotNull @RequestBody User user
  ) throws InvalidFieldsException {
    return service.createUser(user);
  }

  @PutMapping
  public Response updateUser(
    @NotNull @RequestBody User user
  ) throws InvalidFieldsException {

    return service.updateUser(user);
  }

  @DeleteMapping(path = "/{id}")
  public CompleteResponse deleteUser(@PathVariable("id") Long id) throws InvalidFieldsException {
    Response userDeletion = service.deleteUser(id);

    /*
    QueryResponse<Post> userPostsDeletion =
      webClient.build()
      .delete()
      .uri("http://post-service/post/user/" + id)
      .retrieve()
      .bodyToMono(QueryResponse.class)
      .block();

    assert userPostsDeletion != null;
    */

    return new CompleteResponse (
      userDeletion.getUser(),
      null, //userPostsDeletion.queryResult(),
      userDeletion.getMessages() //userPostsDeletion.messages()
    );
  }

}
