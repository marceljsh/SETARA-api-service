package org.synrgy.setara.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.synrgy.setara.common.dto.ErrorResponse;
import org.synrgy.setara.security.service.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final ObjectMapper mapper;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest req,
          @NonNull HttpServletResponse res,
          @NonNull FilterChain filterChain) throws ServletException, IOException {

    final String authHeader = req.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(req, res);
      return;
    }

    final String token = authHeader.substring(7);

    try {
      final String signature = jwtService.extractUsername(token);

      if (signature != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(signature);

        if (jwtService.isTokenValid(token, userDetails)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities());

          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }

    } catch (ExpiredJwtException ex) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      res.setContentType(MediaType.APPLICATION_JSON_VALUE);

      ErrorResponse response = ErrorResponse.from("Token expired", HttpStatus.UNAUTHORIZED);
      mapper.writeValue(res.getWriter(), response);

      return;
    } catch (Exception ex) {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      res.setContentType(MediaType.APPLICATION_JSON_VALUE);

      ErrorResponse response = ErrorResponse.from("Authentication error", HttpStatus.UNAUTHORIZED);
      mapper.writeValue(res.getWriter(), response);

      return;
    }

    filterChain.doFilter(req, res);
  }
}
