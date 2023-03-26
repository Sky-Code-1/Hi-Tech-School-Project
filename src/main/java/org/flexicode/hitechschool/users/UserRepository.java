package org.flexicode.hitechschool.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    public Optional<Users> findByUsername(String username);
}
