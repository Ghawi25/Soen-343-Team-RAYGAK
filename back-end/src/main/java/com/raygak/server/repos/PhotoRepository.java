package com.raygak.server.repos;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raygak.server.models.Photo;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, ObjectId>{
        Optional<Photo> findByUsername(String username);
}
