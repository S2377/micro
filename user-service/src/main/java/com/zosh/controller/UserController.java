package com.zosh.controller;


import com.zosh.modal.User;
import com.zosh.repository.UserRepository;
import com.zosh.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
  private final UserService userService;

    @Autowired // Explicitly inject the UserService dependency
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/adduser")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Get all users (GET)
    @GetMapping("/api/alluser")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);

    }

    // Get a single user by ID (GET)
    @GetMapping("/api/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws Exception {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user,HttpStatus.OK);


    }

    // Update a user (PUT)
    @PutMapping("/api/update/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) throws Exception {
        User user = userService.updateUser(id,updatedUser);
        return new ResponseEntity<>(user,HttpStatus.OK);

    }

    // Delete a user by ID (DELETE)
    @DeleteMapping("/api/deleteuser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws Exception{
        userService.deleteUser(id);

        return new ResponseEntity<>("user deleted successfully",HttpStatus.ACCEPTED);

    }

}
