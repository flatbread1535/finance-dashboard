package com.finance_dashboard.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

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
    // Returns entire HTTP response, data sent back to client is serialized User object
    ResponseEntity<User> findByUserId(@PathVariable Long requestedUserId) {
        Optional<User> userOptional = userRepository.findById(requestedUserId);
        if (userOptional.isPresent()) {
            // Returns successful request (200 OK)
            return ResponseEntity.ok(userOptional.get());
        } else {
            // Generates empty HTTP response with 404 NOT_FOUND status code
            return ResponseEntity.notFound().build();
        }
    }
}
