package com.raygak.server.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String getPhotoByUsername(@PathVariable String username, Model model) {
        Optional<Photo> photo = photoService.getPhotoByUsername(username);
        if (!photo.isPresent())
            return null;
        Photo actualPhoto = photo.get();
        model.addAttribute("image", Base64.getEncoder().encodeToString(actualPhoto.getImage().getData()));
        return "photos";
    }

    @GetMapping("/{id}")
    public String getPhotoById(@PathVariable ObjectId id, Model model) {
        Optional<Photo> photo = photoService.getPhotoById(id);
        if (!photo.isPresent())
            return null;
        Photo actualPhoto = photo.get();
        model.addAttribute("image", Base64.getEncoder().encodeToString(actualPhoto.getImage().getData()));
        return "photos";
    }

    @PostMapping
    public String addPhoto(@RequestParam("username") String username, @RequestParam("image") MultipartFile image,
            Model model) throws IOException {
        photoService.addPhoto(username, image);
        return "photos";
    }
}
