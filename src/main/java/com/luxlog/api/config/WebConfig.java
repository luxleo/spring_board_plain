package com.luxlog.api.config;

import com.luxlog.api.config.resolver.AuthResolverJwt;
import com.luxlog.api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AppConfig appConfig;
    private final SessionRepository sessionRepository;
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AuthIntercepter());
//    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //resolvers.add(new AuthResolver(sessionRepository)); session-cookie방식
        //resolvers.add(new AuthResolver(sessionRepository));
        resolvers.add(new AuthResolverJwt(appConfig));

    }
    //    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5173");
//    }
}
