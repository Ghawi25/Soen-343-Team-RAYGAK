package com.raygak.server.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.raygak.server.models.Photo;
import com.raygak.server.repos.PhotoRepository;

@Service
public class PhotoService {
    
    @Autowired
    private PhotoRepository photoRepository;

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Optional<Photo> getPhotoByUsername(String username) {
        return photoRepository.findByUsername(username);
    }

    public Optional<Photo> getPhotoByUsername(ObjectId id) {
        return photoRepository.findById(id);
    }

    public Photo addPhoto(String username, MultipartFile file) throws IOException {
        Photo photo = new Photo();
        photo.setUsername(username);
        photo.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        photoRepository.insert(photo);
        return photo;
    }
}
