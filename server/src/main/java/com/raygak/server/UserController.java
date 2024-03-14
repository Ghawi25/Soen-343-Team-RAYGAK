package com.raygak.server;

import com.raygak.server.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    // constructor
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    // return a list of all users
    @GetMapping
    public List<User> getAllUserProfiles() {
        return userService.getAllUserProfiles();
    }
    // get a user by Id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserProfileById(@PathVariable(value = "id") String id) {
        User user = userService.getUserProfileById(id)
                .orElseThrow(() -> new RuntimeException("User profile not found for this id :: " + id));
        return ResponseEntity.ok().body(user);
    }
    // create a user
    // Use case 2 - Create user profile
    @PostMapping
    public User createUserProfile(@RequestBody User user) {
        return userService.createUserProfile(user);
    }
    // Use case 3 - Edit user profile
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserProfile(@PathVariable(value = "id") String id, @RequestBody User userDetails) {
        User updatedUserProfile = userService.updateUserProfile(id, userDetails);
        return ResponseEntity.ok(updatedUserProfile);
    }
    // Use case 4 - Delele user profile
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable(value = "id") String id) {
        userService.deleteUserProfile(id);
        return ResponseEntity.ok().build();
    }
}