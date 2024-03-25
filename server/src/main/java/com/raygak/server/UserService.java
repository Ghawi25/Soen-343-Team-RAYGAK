package com.raygak.server;

import com.raygak.server.model.User;
import com.raygak.server.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUserProfiles() {
        return userRepository.findAll();
    }

    public Optional<User> getUserProfileById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserProfileByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUserProfile(User user) {
        // Additional validation and logic before saving
        return userRepository.save(user);
    }

    public User updateUserProfile(String id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User profile not found for this id :: " + id));
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setUserType(userDetails.getUserType());
        return userRepository.save(user);
    }

    public void deleteUserProfile(String id) {
        userRepository.deleteById(id);
    }
}