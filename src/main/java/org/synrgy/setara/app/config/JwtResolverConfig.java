package org.synrgy.setara.app.config;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.synrgy.setara.app.resolver.UserArgumentResolver;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JwtResolverConfig implements WebMvcConfigurer {

  private final UserArgumentResolver userArgumentResolver;

  @Override
  public void addArgumentResolvers(@NotNull List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    resolvers.add(userArgumentResolver);
  }

}
