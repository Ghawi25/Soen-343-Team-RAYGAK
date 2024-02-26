package com.raygak.server.repos;

import com.raygak.server.model.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // You can define custom queries here if needed
    Optional<User> findByEmail(String email);

    // CRUD operations provided by MongoRepository that don't need explicit definition:
    // - save(UserProfile userProfile): Save a userProfile (used for both creating and updating a userProfile).
    // - findById(String id): Find a userProfile by id.
    // - findAll(): Find all userProfiles.
    // - deleteById(String id): Delete a userProfile by id.


}