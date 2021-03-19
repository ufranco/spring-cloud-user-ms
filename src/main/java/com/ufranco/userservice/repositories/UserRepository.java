package com.ufranco.userservice.repositories;

import com.ufranco.userservice.models.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
  boolean existsById(Long id);

  @Query(
    """
    FROM User WHERE UPPER(username) LIKE CONCAT('%', UPPER(?1),'%')
    or UPPER(displayname) LIKE CONCAT('%', UPPER(?1),'%')
    """
  )
  List<User> findAllContainingIgnoreCase(String name, Pageable pageable);

  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
}
