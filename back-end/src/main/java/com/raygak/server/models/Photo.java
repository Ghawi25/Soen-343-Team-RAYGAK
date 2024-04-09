package com.raygak.server.models;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "photos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String username;
    private Binary image;
}
