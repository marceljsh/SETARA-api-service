package org.synrgy.setara.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

  private final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

  @Value("${security.jwt.token.secret-key}")
  private String secretKey;

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);

    return claimsResolver.apply(claims);
  }

  @Override
  public String generateToken(UserDetails userDetails) {
    return generateToken(Map.of(), userDetails);
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    try {
      final String username = extractUsername(token);
      boolean isTokenExpired = isTokenExpired(token);
      boolean isUsernameValid = username.equals(userDetails.getUsername());

      if (isTokenExpired) {
        log.error("token is expired");
        return false;
      }

      if (!isUsernameValid) {
        log.error("user details username (email) is not valid");
        return false;
      }

      return true;

    } catch (MalformedJwtException e) {
      log.error("invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty");
    }

    return false;
  }

  private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    long expirationTime = Long.parseLong(System.getenv("JWT_EXPIRATION"));
    return Jwts.builder()
      .setClaims(extraClaims)
      .setSubject(userDetails.getUsername())
      .setIssuedAt(new Date(System.currentTimeMillis()))
      .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
      .signWith(getSignInKey(), SignatureAlgorithm.HS256)
      .compact();
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSignInKey())
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);

    return Keys.hmacShaKeyFor(keyBytes);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

}
