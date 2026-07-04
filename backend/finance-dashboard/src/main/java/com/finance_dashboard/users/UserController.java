package com.finance_dashboard.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
// Indicates which address requests must have to access this controller
@RequestMapping("/users")
public class UserController {
    
    @GetMapping("/{requestedUserId}")
    ResponseEntity<User> findByUserId(@PathVariable Long requestedUserId) {
        if (requestedUserId.equals(5L)) {
            User user = new User(5L, 
            "adam", 
            "ajlarson0731@gmail.com", 
            "abc123", 
            "937-479-0303");
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
