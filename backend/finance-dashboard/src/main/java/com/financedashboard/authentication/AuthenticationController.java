package com.financedashboard.authentication;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication REST controller responsible for registration 
 * and login requests.
 */
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  /**
   * Constructs an instance of {@code AuthenticationController} with dependency injection.
   *
   * @param authenticationService service layer component that handles core business logic 
   *     for authentication
   */
  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  /**
   * Authenticates an existing user's credentials.
   *
   * @param loginRequest data transfer object containing the user's username and
   *     raw password
   * @return a {@code 200 OK} response with a login response DTO body with the JWT
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
    return ResponseEntity.ok(authenticationService.login(loginRequest));
  }

  /**
   * Registers a new user's account into the application's system.
   *
   * @param registrationRequest data transfer object contining initial account fields
   * @return a {@code OK} response with a login response DTO body with the JWT
   */
  @PostMapping("/register")
  public ResponseEntity<LoginResponse> register(
      @RequestBody @Valid RegistrationRequest registrationRequest) {
    return ResponseEntity.ok(authenticationService.register(registrationRequest));
  }
}
