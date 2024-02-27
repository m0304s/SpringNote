package springSecurity.spring.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.data.redis.core.RedisHash;
//
//import java.util.UUID;
//
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Entity
//public class MemberRefreshToken {
//    @Id
//    private UUID memberId;
//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "member_id")
//    private Member member;
//    private String refreshToken;
//    private int reissueCount = 0;
//
//    public MemberRefreshToken(Member member, String refreshToken) {
//        this.member = member;
//        this.refreshToken = refreshToken;
//    }
//
//    public void updateRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//    }
//
//    public boolean validateRefreshToken(String refreshToken) {
//        return this.refreshToken.equals(refreshToken);
//    }
//
//    public void increaseReissueCount() {
//        reissueCount++;
//    }
//}

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@RedisHash(value = "member_refresh_token",timeToLive = 600)
public class MemberRefreshToken implements Serializable {
    @Id
    private UUID memberId;
    private String refreshToken;
    private int reissueCount = 0;

    public MemberRefreshToken(UUID memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean validateRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public void increaseReissueCount() {
        reissueCount++;
    }
}
