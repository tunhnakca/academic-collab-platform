package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByNumber(String number);

    Optional<User> findByNumber(String number);

    List<User> findByRoleIgnoreCase(String role);


}