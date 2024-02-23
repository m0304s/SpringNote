package springSecurity.spring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Order(0)
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = parseBearerToken(request);
        User user = parseUserSpecification(token);
        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());
        authenticated.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticated);
        filterChain.doFilter(request,response);
    }

    /**
     * parseBearerToken()은 HTTP 요청의 헤더에서 Authorization값을 찾아서 Bearer로 시작하는지 확인하고 접두어를 제외한 토큰값으로 파싱한다. 헤더에 Authorization이 존재하지 않거나 접두어가 Bearer가 아니면 null을 반환한다.
     */
    private String parseBearerToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.substring(0,7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    /**
     * parseUserSpecification()은 토큰값을 토대로 토큰에 담긴 회원ID와 회원타입을 토대로 스프링 시큐리티에서 사용할 User 객체를 반환한다.
     * 이 때 파싱된 토큰이 null이 아니면서 길이가 너무 짧지 않을 때만 토큰을 복호화하고, 그 외에는 별도로 익명임을 뜻하는 User 객체를 생성한다. 비밀번호는 로그인 API를 호출할 때 이미 확인을 했기 때문에, User 객체를 생성할 때는 사용하지 않으므로 빈 문자열을 넘긴다.
     */
    private User parseUserSpecification(String token) {
        String[] split = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(tokenProvider::validateTokenAndGetSubject)
                .orElse("anonymous:anonymous")
                .split(":");

        return new User(split[0], "", List.of(new SimpleGrantedAuthority(split[1])));
    }
}
