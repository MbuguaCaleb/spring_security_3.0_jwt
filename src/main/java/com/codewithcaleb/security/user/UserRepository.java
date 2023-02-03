package com.codewithcaleb.security.user;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//It is like an ORM Layer
//It has methods we can use to query from the database

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

}
