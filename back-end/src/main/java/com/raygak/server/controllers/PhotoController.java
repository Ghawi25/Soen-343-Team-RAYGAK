package com.raygak.server.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.raygak.server.models.Photo;
import com.raygak.server.services.PhotoService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @GetMapping("/user/{username}")
    public ResponseEntity<byte[]> getPhotoByUsername(@PathVariable String username) {
        Optional<Photo> photo = photoService.getPhotoByUsername(username);
        if (!photo.isPresent())
            return null;
        Photo actualPhoto = photo.get();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(actualPhoto.getImage().getData());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable ObjectId id) {
        Optional<Photo> photo = photoService.getPhotoById(id);
        if (!photo.isPresent())
            return null;
        Photo actualPhoto = photo.get();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(actualPhoto.getImage().getData());
    }

    @PostMapping
    public String addPhoto(@RequestParam("username") String username, @RequestParam("image") MultipartFile image) throws IOException {
        photoService.addPhoto(username, image);
        return "ok";
    }

    @PutMapping("/{username}")
    public ResponseEntity<Optional<Photo>> updateUserById(@PathVariable String username, @RequestParam("image") MultipartFile image)  throws IOException {
        return new ResponseEntity<Optional<Photo>>(photoService.updatePhotoByUsername(username, image), HttpStatus.OK);
    }
}
