package com.financedashboard.security;

import com.financedashboard.accounts.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service component responsible for managing JWT.
 */
@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secretKey;

  /**
   * Validates if a token belongs to the matching user and has not expired.
   *
   * @param token the JWT string 
   * @param userDetails the user data to validate against
   * @return {@code true} if the token matches the user identity and is not expired,
   *     or {@code false} otherwise
   */
  public boolean isValid(String token, UserDetails userDetails) {
    String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, claims -> claims.getExpiration());
  }

  /**
   * Extracts the unique username from the token.
   *
   * @param token the JWT string
   * @return the username claim 
   */
  public String extractUsername(String token) {
    return extractClaim(token, claims -> claims.getSubject());
  }

  /**
   * Extracts a specific custom or standard claim.
   *
   * @param <T> the type return mapping of the resolved claim value
   * @param token the JWT string
   * @param resolver functional mapping strategy to execute against token claims
   * @return the extracted claim value
   */
  public <T> T extractClaim(String token, Function<Claims, T> resolver) {
    Claims claims = extractAllClaims(token);
    return resolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token)
        .getPayload();
  }

  /**
   * Generates a new JWT for an authenticated account.
   *
   * @param account the Account entity to be given a JWT
   * @return a JWT string
   */
  public String generateToken(Account account) {
    String token = Jwts.builder()
        .subject(account.getUsername())
        .claim("accountId", account.getAccountId())
        .claim("role", account.getRole())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
        .signWith(getSignInKey())
        .compact();

    return token;
  }

  private SecretKey getSignInKey() {
    String secret = secretKey;

    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
