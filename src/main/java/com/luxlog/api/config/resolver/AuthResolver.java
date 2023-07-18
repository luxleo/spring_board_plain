package com.luxlog.api.config.resolver;

import com.luxlog.api.config.data.UserSession;
import com.luxlog.api.domain.Session;
import com.luxlog.api.exception.UnauthorizedException;
import com.luxlog.api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {
    private final SessionRepository sessionRepository;
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
        //String accessToken = webRequest.getHeader("Authorization");
        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (nativeRequest == null) {
            log.error("nativeRequest is null");
            throw new UnauthorizedException();
        }
        Cookie[] cookies = nativeRequest.getCookies();
        if (cookies.length == 0) {
            log.error("no cookie");
            throw new UnauthorizedException();
        }
        String accessToken = cookies[0].getValue();
        if (accessToken == null || accessToken.equals("")) {
            log.info("acceesToken not valid : {}", accessToken);
            throw new UnauthorizedException();
        }
        log.info(">>>>> pass");
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(UnauthorizedException::new);
        return new UserSession(session.getUser().getId());
    }

}
