package com.ufranco.userservice.services;

import com.ufranco.userservice.models.dto.QueryParams;
import com.ufranco.userservice.models.dto.Response;
import com.ufranco.userservice.models.dto.UserQueryResponse;
import com.ufranco.userservice.models.entities.User;
import com.ufranco.userservice.models.exceptions.InvalidField;
import com.ufranco.userservice.models.exceptions.InvalidQueryParametersException;
import com.ufranco.userservice.models.exceptions.InvalidUserFieldsException;
import com.ufranco.userservice.models.exceptions.abstracts.InvalidFieldsException;
import com.ufranco.userservice.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserService {

  @Autowired
  UserRepository repository;

  public UserQueryResponse getUsers(QueryParams params) throws InvalidQueryParametersException {
    validateForQuery(params);

    Sort sort = Sort.by(
      Sort.Direction.fromString(params.order()),
      params.sortBy()
    );

    Pageable pageable = PageRequest.of(
      params.page(),
      params.limit(),
      sort
    );

    log.debug("Executing User query under parameters: \n  " + params);

    List<User> queryResult = params.name().isBlank() ?
      repository.findAll(pageable).getContent() :
      repository.findAllContainingIgnoreCase(params.name(), pageable);

    return new UserQueryResponse(
      queryResult,
      queryResult.size(),
      List.of()
    );

  }

  public Response getUser(Long id) throws InvalidUserFieldsException {
    validateForGet(id);

    User user = repository.findById(id).orElse(null);

    return new Response(user, List.of());
  }


  public Response createUser(User user) throws InvalidUserFieldsException {
    validateForCreate(user);

    user = repository.save(user);

    return new Response(user, List.of("User was created successfully."));
  }

  public Response updateUser(User user) throws InvalidFieldsException {
    validateForUpdate(user);

    user = repository.save(user);

    return new Response(user, List.of("User was updated successfully"));
  }

  public Response deleteUser(Long id) throws InvalidUserFieldsException {

    validateForDelete(id);

    repository.deleteById(id);

    User userDeleted = new User();
    userDeleted.setId(id);
    return new Response(
      userDeleted,
      List.of("User was deleted successfully.")
    );
  }

  private void validateForQuery(QueryParams params) throws InvalidQueryParametersException {
    List<InvalidField> invalidFields = new ArrayList<>();
    validatePaginationParameters(params, invalidFields);
    validateSortParameters(params, invalidFields);

    if (!invalidFields.isEmpty()) throw new InvalidQueryParametersException(invalidFields);
  }

  private void validatePaginationParameters(QueryParams params, List<InvalidField> invalidFields) {
    if (params.page() < 0) {
      invalidFields.add(
        new InvalidField(
          "page",
          List.of("Page number cannot be negative.")
        )
      );
    }

    if(params.limit() < 1) {
      invalidFields.add(
        new InvalidField(
          "limit",
          List.of("Page limit cannot be negative.")
        )
      );
    } else if(params.limit() > 100) {
      invalidFields.add(
        new InvalidField(
          "limit",
          List.of("Page limit is too large.")
        )
      );
    }
  }

  private void validateSortParameters(QueryParams params, List<InvalidField> invalidFields) {
    if (!(params.order().equals("asc") || params.order().equals("desc"))) {
      invalidFields.add(
        new InvalidField("order",
          List.of("Order parameter does not meet the requirements to make the query.")
        )
      );

    }
    if(params.sortBy().equalsIgnoreCase("password")) {

      invalidFields.add(
        new InvalidField("sortBy",
          List.of("Sort By field not valid.")
        )
      );
    } else {
      try {
        User.class.getDeclaredField(params.sortBy().toLowerCase());
      } catch (NoSuchFieldException e) {

        invalidFields.add(
          new InvalidField("sortBy",
            List.of("SortBy field does not exist.")
          )
        );
      }
    }
  }

  private void validateForGet(Long id) throws InvalidUserFieldsException {
    List<InvalidField> invalidFields = new ArrayList<>();

    idValidator(id, invalidFields);

    if(!invalidFields.isEmpty()) throw new InvalidUserFieldsException(invalidFields);
  }
  private void validateForCreate(User user) throws InvalidUserFieldsException {
    List<InvalidField> invalidFields = new ArrayList<>();

    if(user.getId() != null) {
      invalidFields.add(
        new InvalidField(
          "id",
          List.of("Id cannot be manually defined"))
      );
    }

    usernameValidator(user.getUsername(), invalidFields);
    displaynameValidator(user.getDisplayname(), invalidFields);
    emailValidator(user.getEmail(), invalidFields);
    passwordValidator(user.getPassword(), invalidFields);

    if(invalidFields.isEmpty()) {
      usernameAvailabilityValidator(user, invalidFields);
      emailAvailabilityValidator(user, invalidFields);
    }

    if (!invalidFields.isEmpty()) throw new InvalidUserFieldsException(invalidFields);

  }

  private void validateForUpdate(User user) throws InvalidUserFieldsException {
    List<InvalidField> invalidFields = new ArrayList<>();

    idValidator(user.getId(), invalidFields);
    usernameValidator(user.getUsername(), invalidFields);
    displaynameValidator(user.getDisplayname(), invalidFields);
    emailValidator(user.getEmail(), invalidFields);
    passwordValidator(user.getPassword(), invalidFields);

    if(invalidFields.isEmpty()) {
      User userInDb = repository.findById(user.getId()).orElse(null);

      assert userInDb != null;

      if(user.getModifiedAt().before(userInDb.getModifiedAt())){
        invalidFields.add(
          new InvalidField(
            "modifiedAt",
              List.of("Version mismatch between data provided and data stored.")
            )
        );
      }

      if(!userInDb.getUsername().equals(user.getUsername())) {
        usernameAvailabilityValidator(user, invalidFields);
      }

      if(!userInDb.getEmail().equals(user.getEmail())) {
        emailAvailabilityValidator(user, invalidFields);
      }
    }

    if(!invalidFields.isEmpty()) throw new InvalidUserFieldsException(invalidFields);

  }

  private void validateForDelete(Long id) throws InvalidUserFieldsException {
    List<InvalidField> invalidFields = new ArrayList<>();

    idValidator(id, invalidFields);

    if (!invalidFields.isEmpty()) throw new InvalidUserFieldsException(invalidFields);
  }

  private void idValidator(Long id, List<InvalidField> invalidFields) {
    List<String> messages = new ArrayList<>();

    if ( id == null || id < 1) {
      messages.add("Please provide a valid id.");

    } else if (!repository.existsById(id)) {
      messages.add("Id provided does not belong to any user.");

    } else return;

    invalidFields.add(new InvalidField("id", messages));
  }

  private void usernameAvailabilityValidator(User user, List<InvalidField> invalidFields) {
    if(repository.existsByUsername(user.getUsername())) {
      invalidFields.add(
        new InvalidField("username", List.of("Username already taken."))
      );
    }
  }

  private void emailAvailabilityValidator(User user, List<InvalidField> invalidFields) {
    if(repository.existsByEmail(user.getEmail())) {
      invalidFields.add(
        new InvalidField("email", List.of("Email already taken."))
      );
    }
  }

  private void usernameValidator(String username, List<InvalidField> invalidFields) {
    List<String> messages = new ArrayList<>();

    if (username == null || username.isBlank()) {
      messages.add("Please add an username.");
    } else {
      if (!Pattern.matches("^.{8,30}$", username)) {
        messages.add("Username must have at least 6 characters and less than 30.");
      }
      if (Pattern.matches("^.*[^a-zA-Z0-9\\s]+.*$", username)) {
        messages.add("Username must not have special characters nor blank spaces");
      }
    }

    if(!messages.isEmpty()) {
      invalidFields.add(
        new InvalidField("username", messages)
      );
    }
  }

  private void displaynameValidator(String displayname, List<InvalidField> invalidFields) {
    List<String> messages = new ArrayList<>();

    if (displayname == null || displayname.isBlank()) {
      messages.add("Please add a displayname.");
    } else if (displayname.length() > 30) {
      messages.add("Displayname cannot be larger than 30 characters.");
    }

    if(!messages.isEmpty()) {
      invalidFields.add(
        new InvalidField("displayname", messages)
      );
    }
  }

  private void emailValidator(String email, List<InvalidField> invalidFields) {
    if (
      email == null ||
      email.isBlank() ||
      !Pattern.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)
    ) {
      invalidFields.add(
        new InvalidField(
          "email",
          List.of("Please add a valid email.")
      ));
    }


  }

  private void passwordValidator(String password, List<InvalidField> invalidFields) {
    List<String> messages = new ArrayList<>();

    if (password == null || password.isBlank()) {
      messages.add("Please add a password");

    } else {

      if (!Pattern.matches("^.{8,50}$", password)) {
        messages.add("Password must have at least 8 characters and less than 50.");
      }
      if (!Pattern.matches("^.*[0-9].*[0-9].*$", password)) {
        messages.add("Password must have at least two digits.");
      }
      if (!Pattern.matches("^.*[a-z].*$", password)) {
        messages.add("Password must have at least one lowercase character.");
      }
      if (!Pattern.matches("^.*[A-Z].*$", password)) {
        messages.add("Password must have at least one uppercase character.");
      }
      if (Pattern.matches("^.*\\s|\\n.*$", password)) {
        messages.add("Password must not contain white spaces.");
      }
      if (!Pattern.matches("^.*[^a-zA-Z0-9].*$", password)) {
        messages.add("Password must have at least one special character.");
      }
    }

    if(!messages.isEmpty()) {
      invalidFields.add(
        new InvalidField("password", messages)
      );
    }

  }
}
