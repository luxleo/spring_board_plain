# web mvc part

## 0. request Param 받는 3가지 방법 
[@RequestParam(form data,get queryParam), @ResponseBody(json)]
## 1. validation - starter-validation 라이브러리 등록
    @NotBlank등을 필드에 추가 시켜주고 파라미터에 @Valid 추가
## 2. @NotBlank(message = "custom message") 등록 가능하다
## 3. @Valid에 입밴 당하면 원래는 함수 로직으로 내려오지 않지만 
    BindingResult를 파라메터에 남겨 둠으로써 가능하다.
### 3-1. BindingResult를 제거하고 @ControllerAdvice 어노테이션으로 Controller Advice를 정의 해주면 
    모든 컨트롤러 메서드에 모든 에러 처리하는 수고를 덜 수있다.
## 4. 빌더의 장점:
```text
    1.가독성이 좋다(많은 필드가 있을 경우 더 부각된다.)
    2.값 생성에 대한 유연함(순서 지킬 필요 없어서)
    3.필요한 값만 받을 수있다.
    ??4.객체의 불변성?
```

## 5. jpa 
    사용시 transaction 안에서 생성한 포스트를 persist로 저장시 같은 객체를 참조한다. 
## 6. gradle -> task -> validation에서 전체 테스트 시행 가능.
## 7. domain에 service용 getter 만들면 
    굉장히 꼬인다. 고로 response객체를 따로 만들어 처리하자       
## 8. intellj -> preference -> import optimizer 해주면 안쓰는 패키지 자동 정리
## 9. 전체 조회 응답 반환시 response객체로 타입변환하여 넘길때는 stream을 사용하자 
    case1: builder
    return postRepository.findAll().stream()
        .map(post ->
                (PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .build()
        ))   
        .collect(Collectors.toList());
        
    case2: overload constructor
    return postRepository.findAll().stream()
        .map(post->new PostResponse(post)
        .collect(Collectors.toList());
        
    case3:축약형
    return postRepository.findAll().stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());
## 10. repository.saveAll()메서드 && List.of(el1,el2...) 이용하여 복수 데이터 저장하기
        postRepository.saveAll(List.of(
            Post.builder()
                    .title("foo1")
                    .content("bar1")
                    .build(),
            Post.builder()
                    .title("foo2")
                    .content("bar2")
                    .build()
        ));
## 11. pagination이 필요한 이유
    1. 한번에 DB서버에 너무 많은 조회시(100,000,000) db서버가 뻗어 버릴수도 ...
    2. db서버에서 애플리케이션 서버로 보내줄때 통신 비용이 어마무시
    3. 어플리케이션 서버에 메모리가 급격히 늘어남...
## 12. 테스트시 브레이크 포인트 걸고 evaluate실행하면
    해당 테스트 컨텍스트 내에서 값 조회 가능
## 13. mapToObj로 다른 클래스로 매핑하자
    List<Post> posts = IntStream.range(1, 31)
        .mapToObj(i -> Post.builder()
                .title("title " + i)
                .content("content " + i)
                .build()
        ).collect(Collectors.toList());
## 14. @ControllerAdvice에 핸들러 등록시 
    상위 Exception클래스를 생성하고 그 상위 클래스를 상속받은 자식클래스들을 처리하게 하여 코드 양을 줄인다.
## 15. DTO내부에 validator메소드를 두어 체크 하고 바로 Exception을 throw하도록 처리하면 좋다.
## 16. @ControllerAdvice에서 @Builder를 이용하여 validations Map을 두어 json으로 exception메시지 던져 줄때 필요한 경우만 삽입
## 17. API 인증의 방법
### 1.interceptor : 인증(authorization)시 인터셉터를 이용하여 request.getHeader() || request.getParameter로 필요한 값 조회
    public class AuthIntercepter implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            log.info("handler called");
            String authHeader = request.getParameter("authorization");
            if(authHeader!=null && authHeader.equals("luxlog")) return true;
    
            throw new UnauthorizedException();
        }
    
        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            log.info("postHandle called");
        }
    
        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            log.info("afterCompletion called");
        }
    }
    이후 webconfig에 등록한다.
### 2. methodArgsResolver로 특정 DTO를 포함할때만 진행
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
            String access = webRequest.getParameter("access");
            if (access == null || access.equals("")) {
                throw new UnauthorizedException();
            }
            UserSession userSession = new UserSession();
            userSession.setName(access);
            return userSession;
        }
    }
## 18. 메모리 디비-data.sql
    resources/import.sql로 사용할 임시 데이터
    생성가능하다. 
## cookie-session 방식 인증
    쿠키의 각종 설정값(same-site, httpOnly)등 이해하기
    쿠키에 실을 세션 accesstoken값 UUID로 구현
## jwt 방식 인증
    1.Base64: byte->str, str->byte로
    2.jwt 암호화 방식: singing key로 암호화할 대상(subject) 바디에 실어 전달
        2-1HSA256(bit) -> 32byte
        2-2HSA512(bit) -> 64byte
## 비밀 번호 암호화
    1.해시의 방식을 설명하세요
        SHA1, SHA256, MD5 -> 왜 이런 알고리즘으로 하면 안되는지
    2. 왜 Bcrypt,Scrypt,Argon2를 왜 사용해야하는지
    3. 1에 대한 응답은: salt이다. 해시는 인풋이 같으면 아웃풋이 같으므ㅇ
## @Profile, @ActiveProfile
    1.테스트와 운영환경의 프로필에 따른 설정 가능케 한다-> passwordEncoder에서 사용
##  다형성
    1. PasswordEncoder와 같이 인터페이스의 구현체를 상황별로 달리둔다.
        예) ScryptPasswordEncoder = default
            PlainPasswordEncdoer = test 이런식으로 말이다.
# 단축키
```text
1. class에서 annotation으로 메서드생성시에 cmd+7하면
    자동으로 생성된 메서드들 참조가능하다.
```