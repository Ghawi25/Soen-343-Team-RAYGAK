package com.raygak.server.controllers;

import com.raygak.server.models.*;
import com.raygak.server.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable ObjectId id) {
        return new ResponseEntity<Optional<User>>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Optional<User>> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<Optional<User>>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, String> payload) {
        User user = userService.createUser(payload.get("email"), payload.get("password"), payload.get("userType"));
        if(user == null) {
            return new ResponseEntity<User>(user, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<User>(userService.createUser(payload.get("email"), payload.get("password"), payload.get("userType")), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable ObjectId id) {
        userService.deleteUserById(id);
        return new ResponseEntity<String>("Deleted user", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<User>> updateUserById(@PathVariable ObjectId id, @RequestBody Map<String, String> payload) {
        return new ResponseEntity<Optional<User>>(userService.updateUserById(id, payload.get("email"), payload.get("password"), payload.get("userType")), HttpStatus.OK);
    }

//    private final UserService userService;
//    private final UserRepository userRepository;
//    // constructor
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//    // return a list of all users
//    @GetMapping
//    public List<User> getAllUserProfiles() {
//        return userService.getAllUserProfiles();
//    }
//    // get a user by Id
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserProfileById(@PathVariable(value = "id") String id) {
//        User user = userService.getUserProfileById(id)
//                .orElseThrow(() -> new RuntimeException("User profile not found for this id :: " + id));
//        return ResponseEntity.ok().body(user);
//    }
//    // create a user
//    @PostMapping
//    public User createUserProfile(@RequestBody User user) {
//        return userService.createUserProfile(user);
//    }
//    // update a user
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUserProfile(@PathVariable(value = "id") String id, @RequestBody User userDetails) {
//        User updatedUserProfile = userService.updateUserProfile(id, userDetails);
//        return ResponseEntity.ok(updatedUserProfile);
//    }
//    // delete a user
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUserProfile(@PathVariable(value = "id") String id) {
//        userService.deleteUserProfile(id);
//        return ResponseEntity.ok().build();
//    }
}