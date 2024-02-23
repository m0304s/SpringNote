package springSecurity.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springSecurity.spring.common.MemberType;
import springSecurity.spring.entity.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByAccount(String account);
    List<Member> findAllByType(MemberType type);
}
