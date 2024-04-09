package com.raygak.server.repos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.raygak.server.models.Photo;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, ObjectId>{

}
