package org.launchcode.capstonebackend.models.data;

import org.launchcode.capstonebackend.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {


}
