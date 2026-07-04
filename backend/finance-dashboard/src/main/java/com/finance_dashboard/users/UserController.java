package com.finance_dashboard.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
// Indicates which address requests must have to access this controller
@RequestMapping("/users")
public class UserController {

    // Marks method as handler method with GET requests.
    public String requestMethodName(@RequestParam String param) {
        return new String();
    }
    
    @GetMapping("/{requestedUserId}")
    ResponseEntity<String> findByUserId() {
        return ResponseEntity.ok("{}");
    }
}
