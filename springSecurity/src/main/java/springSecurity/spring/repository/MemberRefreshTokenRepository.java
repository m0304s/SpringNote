package springSecurity.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import springSecurity.spring.entity.MemberRefreshToken;

import java.util.Optional;
import java.util.UUID;

//public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, UUID> {
//    Optional<MemberRefreshToken> findByMemberIdAndReissueCountLessThan(UUID id, long count);
//}

public interface MemberRefreshTokenRepository extends CrudRepository<MemberRefreshToken, UUID> {
    Optional<MemberRefreshToken> findByMemberId(UUID id);
}
