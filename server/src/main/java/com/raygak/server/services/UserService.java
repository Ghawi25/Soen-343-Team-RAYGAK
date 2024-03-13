package com.raygak.server.services;

import com.raygak.server.models.User;
import com.raygak.server.repos.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public User createUser(String email, String password, String userType) {
        User user = new User(email, password, userType);
        if(userRepository.existsByEmail(email)) {
            userRepository.insert(user);
            return user;
        }

        return null;
    }

    public void deleteUserById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public Optional<User> updateUserById(ObjectId id, String email, String password, String userType) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User user = optionalUser.get();
        user.setEmail(email);
        user.setPassword(password);
        user.setUserType(userType);
        return Optional.of(userRepository.save(user));
    }
//    private final UserRepository userRepository;
//
//    @Autowired
//    public UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    public List<User> getAllUserProfiles() {
//        return userRepository.findAll();
//    }
//
//    public Optional<User> getUserProfileById(String id) {
//        return userRepository.findById(id);
//    }
//
//    public Optional<User> getUserProfileByEmail(String email) {
//        return userRepository.findByEmail(email);
//    }
//
//    public User createUserProfile(User user) {
//        // Additional validation and logic before saving
//        return userRepository.save(user);
//    }
//
//    public User updateUserProfile(String id, User userDetails) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User profile not found for this id :: " + id));
//        user.setEmail(userDetails.getEmail());
//        user.setPassword(userDetails.getPassword());
//        user.setUserType(userDetails.getUserType());
//        return userRepository.save(user);
//    }
//
//    public void deleteUserProfile(String id) {
//        userRepository.deleteById(id);
//    }
}