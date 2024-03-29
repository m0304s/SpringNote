package springSecurity.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springSecurity.spring.dto.member.request.MemberUpdateRequest;
import springSecurity.spring.dto.member.response.MemberDeleteResponse;
import springSecurity.spring.dto.member.response.MemberInfoResponse;
import springSecurity.spring.dto.member.response.MemberUpdateResponse;
import springSecurity.spring.repository.MemberRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(UUID id){
        return memberRepository.findById(id)
                .map(MemberInfoResponse::from)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
    }
    @Transactional
    public MemberDeleteResponse deleteMember(UUID id){
        if(!memberRepository.existsById(id))
            return new MemberDeleteResponse(false);
        memberRepository.deleteById(id);
        return new MemberDeleteResponse(true);
    }
    @Transactional
    public MemberUpdateResponse updateMember(UUID id, MemberUpdateRequest request){
        return memberRepository.findById(id)
                .filter(member -> member.getPassword().equals(request.password()))
                .map(member -> {
                    member.update(request,encoder);
                    return MemberUpdateResponse.of(true,member);
                })
                .orElseThrow(() -> new NoSuchElementException("아이디 또는 비밀번호가 일치하지 않습니다."));
    }
}
