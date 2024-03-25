package com.raygak.server;

import com.raygak.server.models.User;
import com.raygak.server.repos.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService1 {
    private final UserRepository userRepository;

    @Autowired
    public UserService1(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUserProfiles() {
        return userRepository.findAll();
    }

    public Optional<User> getUserProfileById(String id) {
        ObjectId objId = new ObjectId(id);
        return userRepository.findById(objId);
    }

    public Optional<User> getUserProfileByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUserProfile(User user) {
        // Additional validation and logic before saving
        return userRepository.save(user);
    }

    public User updateUserProfile(String id, User userDetails) {
        // findById takes ObjectId BUT it was getting passed a string before.
        // This is a quickfix, not sure if it works.
        ObjectId objId = new ObjectId(id);
        User user = userRepository.findById(objId)
                .orElseThrow(() -> new RuntimeException("User profile not found for this id :: " + id));
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        user.setUserType(userDetails.getUserType());
        return userRepository.save(user);
    }

    public void deleteUserProfile(String id) {
        ObjectId objId = new ObjectId(id);
        userRepository.deleteById(objId);
    }
}