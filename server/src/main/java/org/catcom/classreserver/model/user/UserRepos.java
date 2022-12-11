package org.catcom.classreserver.model.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepos extends CrudRepository<User, Integer>
{
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);
}
