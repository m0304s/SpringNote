package springSecurity.spring.entity;

import jakarta.persistence.*;
import lombok.*;
import springSecurity.spring.common.MemberType;
import springSecurity.spring.dto.member.request.MemberUpdateRequest;
import springSecurity.spring.dto.sign_up.request.SignUpRequest;

import java.util.UUID;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Member {
    @Column(nullable = false,unique = true)
    private String account;
    @Column(nullable = false)
    private String password;
    private String name;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private MemberType type;
    private LocalDateTime createdAt;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public static Member from(SignUpRequest request){
        return Member.builder()
                .account(request.account())
                .password(request.password())
                .name(request.name())
                .age(request.age())
                .type(MemberType.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }
    public void update(MemberUpdateRequest newMember){
        this.password= newMember.newPassword()==null || newMember.newPassword().isBlank()? this.password : newMember.password();
        this.name = newMember.name();
        this.age = newMember.age();
    }
}
