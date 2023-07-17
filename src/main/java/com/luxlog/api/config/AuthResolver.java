package com.luxlog.api.config;

import com.luxlog.api.config.data.UserSession;
import com.luxlog.api.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {
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
        String userName = webRequest.getHeader("Authorization");
        if (userName == null || userName.equals("")) {
            throw new UnauthorizedException();
        }
        return new UserSession(userName);
    }
}
