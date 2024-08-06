package org.synrgy.setara.app.resolver;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;
import org.synrgy.setara.security.service.JwtService;
import org.synrgy.setara.user.model.User;
import org.synrgy.setara.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

  private final JwtService jwtService;

  private final UserRepository userRepo;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return User.class.equals(parameter.getParameterType());
  }
  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getNativeRequest();

    String authorization = servletRequest.getHeader("Authorization");
    if (authorization == null) {
      return null;
    }

    String token = authorization.replace("Bearer ", "");
    if (token.isEmpty()) {
      return null;
    }

    String signature = jwtService.extractUsername(token);
    User user = userRepo.findBySignature(signature)
        .orElseThrow(() -> new UsernameNotFoundException("User signature " + signature + " not found"));
    if (!jwtService.isTokenValid(token, user)) {
      throw new ExpiredJwtException(null, null, "Token expired");
    }

    return user;
  }

}
