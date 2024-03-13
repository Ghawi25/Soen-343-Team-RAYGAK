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
}