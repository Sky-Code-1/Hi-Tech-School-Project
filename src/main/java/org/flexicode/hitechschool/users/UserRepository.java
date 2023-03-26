package org.flexicode.hitechschool.users;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {
    public Optional<Users> findByUsername(String username);
}
