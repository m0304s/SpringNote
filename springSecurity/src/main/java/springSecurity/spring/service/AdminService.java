package springSecurity.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springSecurity.spring.common.MemberType;
import springSecurity.spring.dto.member.response.MemberInfoResponse;
import springSecurity.spring.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getMembers(){
        return memberRepository.findAllByType(MemberType.USER).stream()
                .map(MemberInfoResponse::from)
                .toList();
    }
    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getAdmins(){
        return memberRepository.findAllByType(MemberType.ADMIN).stream()
                .map(MemberInfoResponse::from)
                .toList();
    }
}
