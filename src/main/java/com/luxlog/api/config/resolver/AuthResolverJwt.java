package com.luxlog.api.config.resolver;

import com.luxlog.api.config.data.UserSession;
import com.luxlog.api.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;

@Slf4j
@RequiredArgsConstructor
public class AuthResolverJwt implements HandlerMethodArgumentResolver {
    private final static String JWT_PRIVATE_KEY = "cd7+/e8dlsaOiRldLN5KCqrojWXKXnBr4KeFDq9ab/s=";
    /**
     * parameter로 넘어온 값이 실제 사용하려는 DTO클래스와 형식이 일치하는지 확인
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    /**
     * 위에 거 true이면 처리한다.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");

        if (jws == null || jws.equals("")) {
            throw new UnauthorizedException();
        }
        byte[] decodeByteKey = Base64.getDecoder().decode(JWT_PRIVATE_KEY);
        try {
            Jws<Claims> claim = Jwts.parserBuilder()
                    .setSigningKey(decodeByteKey)
                    .build()
                    .parseClaimsJws(jws);
            log.info(">>>> claim:{}",claim);
            String userId = claim.getBody().getSubject();
            return new UserSession(Long.parseLong(userId));
        } catch (JwtException e) {

            throw new UnauthorizedException();
        }
    }

}
