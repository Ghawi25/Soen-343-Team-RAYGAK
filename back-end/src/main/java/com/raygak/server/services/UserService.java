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

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserById(ObjectId id) {
        return userRepository.findById(id);
    }

    public User createUser(String username, String password, String userType) {
        User user = new User(username, password, userType);
        if(userRepository.existsByUsername(username)) {
            return null;
        }
        userRepository.insert(user);
        return user;
    }

    public void deleteUserById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public Optional<User> updateUserById(ObjectId id, String username, String password, String userType) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()) {
            return Optional.empty();
        }
        User user = optionalUser.get();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserType(userType);
        return Optional.of(userRepository.save(user));
    }
}