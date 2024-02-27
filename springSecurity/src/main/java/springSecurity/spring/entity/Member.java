package springSecurity.spring.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;
import springSecurity.spring.common.MemberType;
import springSecurity.spring.dto.member.request.MemberUpdateRequest;
import springSecurity.spring.dto.sign_up.request.SignUpRequest;

import java.util.UUID;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {
    @Column(nullable = false, scale = 20, unique = true)
    private String account;
    @Column(nullable = false)
    private String password;
    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private MemberType type;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public static Member from(SignUpRequest request, PasswordEncoder encoder) {
        return Member.builder()
                .account(request.account())
                .password(encoder.encode(request.password()))
                .name(request.name())
                .age(request.age())
                .type(MemberType.USER)
                .build();
    }

    @Builder
    private Member(String account, String password, String name, Integer age, MemberType type) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.age = age;
        this.type = type;
    }

    public void update(MemberUpdateRequest newMember, PasswordEncoder encoder) {
        this.password = newMember.newPassword() == null || newMember.newPassword().isBlank()
                ? this.password : encoder.encode(newMember.newPassword());
        this.name = newMember.name();
        this.age = newMember.age();
    }
}