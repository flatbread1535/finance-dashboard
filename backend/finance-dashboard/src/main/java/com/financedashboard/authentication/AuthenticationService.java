package com.financedashboard.authentication;

import com.financedashboard.accounts.Account;
import com.financedashboard.accounts.AccountCreateRequest;
import com.financedashboard.accounts.AccountRepository;
import com.financedashboard.accounts.AccountService;
import com.financedashboard.exceptions.ResourceNotFoundException;
import com.financedashboard.exceptions.ValidationException;
import com.financedashboard.security.JwtService;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service layer component that handles core business logic for authentication.
 */
@Service
public class AuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final AccountService accountService;
  private final AccountRepository accountRepository;
  private final JwtService jwtService;

  /**
   * Constructs an instance of {@code AuthenticationService} with dependency injection.
   *
   * @param authenticationManager the Spring Security manager used to verify user credentials
   * @param accountService the service layer responsible for handing account business logic
   * @param accountRepository the data access layer responsible for retrieving accounts
   * @param jwtService the utility provider managing JWT generation
   */
  public AuthenticationService(
      AuthenticationManager authenticationManager,
      AccountService accountService, 
      AccountRepository accountRepository,
      JwtService jwtService
  ) {
    this.authenticationManager = authenticationManager;
    this.accountService = accountService;
    this.accountRepository = accountRepository;
    this.jwtService = jwtService;
  }

  /**
   * Authenticates a user's credentials, updates account metadata, 
   * and issues an access token.
   *
   * @param request data transfer object containing credentials for authentication
   * @return the login response data transfer object with the JWT
   * @throws ValidationException if credentials fail verification or the username is not found
   * @throws ResourceNotFoundException if the account entity cannot be found
   */
  public LoginResponse login(LoginRequest request) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.username(), request.password()));
    } catch (UsernameNotFoundException e) {
      throw new ValidationException(Map.of("username", "Username not found"));
    } catch (BadCredentialsException e) {
      throw new ValidationException(Map.of("password", "Incorrect password"));
    }

    Account account = accountRepository.findByUsername(request.username())
        .orElseThrow(() -> new ResourceNotFoundException("Could not find account."));

    account.setLastLoginTime(LocalDateTime.now());
    accountRepository.save(account);

    String token = jwtService.generateToken(account);

    return new LoginResponse(
        account.getAccountId(), 
        account.getRole(), 
        account.getUsername(),
        token
    );
  }

  /**
   * Registers a brand new user.
   *
   * @param request data transfer object containing username and raw password
   * @return a data transfer object response containing account details and a JWT
   */
  public LoginResponse register(RegistrationRequest request) {

    Account account = accountService
        .createAccount(new AccountCreateRequest(request.username(), request.password()));

    String token = jwtService.generateToken(account);

    return new LoginResponse(
        account.getAccountId(), 
        account.getRole(), 
        account.getUsername(),
        token
    );
  }
}
