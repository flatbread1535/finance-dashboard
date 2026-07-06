package com.finance_dashboard.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

// Declares class as controller to Spring
@RestController
// Indicates which address requests must have to access this controller
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    // Passes database repository into controller
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Maps incoming HTTP GET requests to method
    @GetMapping("/{requestedUserId}")
    // Returns entire HTTP response, data sent back to client is serialized User
    // object
    public ResponseEntity<User> findByUserId(@PathVariable Long requestedUserId) {
        Optional<User> userOptional = userRepository.findById(requestedUserId);
        if (userOptional.isPresent()) {
            // Returns successful request (200 OK)
            return ResponseEntity.ok(userOptional.get());
        } else {
            // Generates empty HTTP response with 404 NOT_FOUND status code
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping()
    private ResponseEntity<Iterable<User>> findAll() {
         return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody User newUserRequest, UriComponentsBuilder ucb) {
        User savedUser = userRepository.save(newUserRequest);
        URI locationOfNewUser = ucb.path("/users/{userId}")
                .buildAndExpand(savedUser.getUserId()).toUri();
        return ResponseEntity.created(locationOfNewUser).build();
    }

    @PutMapping("/{requestedUserId}")
    public ResponseEntity<Void> putUser(@PathVariable Long requestedUserId, @RequestBody User userUpdate) {
        Optional<User> optionalUser = userRepository.findById(requestedUserId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = optionalUser.get();
        existingUser.setUsername(userUpdate.getUsername());
        existingUser.setEmail(userUpdate.getEmail());
        existingUser.setHashPassword(userUpdate.getHashPassword());
        existingUser.setPhoneNumber(userUpdate.getPhoneNumber());

        userRepository.save(existingUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{requestedUserId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long requestedUserId) {
        
        if (!userRepository.existsById(requestedUserId)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(requestedUserId);
        return ResponseEntity.noContent().build();
    }
}
