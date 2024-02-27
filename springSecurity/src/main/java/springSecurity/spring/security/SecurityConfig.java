package springSecurity.spring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint entryPoint;
    private final String [] allowedUrls = {"/", "/swagger-ui/**", "/v3/**"};
    private static final String[] ALLOWED_URIS = {"/sign-up", "/sign-in"};
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()))    //H2 콘솔 사용을 위한 설정
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(ALLOWED_URIS).permitAll()	// requestMatchers의 인자로 전달된 url은 모두에게 허용
                        .requestMatchers(PathRequest.toH2Console()).permitAll()	// H2 콘솔 접속은 모두에게 허용
                        .anyRequest().authenticated()	// 그 외의 모든 요청은 인증 필요
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
                .build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
