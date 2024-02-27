package springSecurity.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import springSecurity.spring.common.MemberType;
import springSecurity.spring.entity.Member;
import springSecurity.spring.repository.MemberRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class AdminInitializer implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    @Override
    public void run(ApplicationArguments args) {
        memberRepository.save(Member.builder()
                .account("admin")
                .password(encoder.encode("admin"))
                .name("관리자")
                .type(MemberType.ADMIN)
                .build());
    }
}
