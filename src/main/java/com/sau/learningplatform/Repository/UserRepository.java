package com.sau.learningplatform.Repository;

import com.sau.learningplatform.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
