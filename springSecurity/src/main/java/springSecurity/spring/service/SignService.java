package springSecurity.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springSecurity.spring.dto.sign_in.request.SignInRequest;
import springSecurity.spring.dto.sign_in.response.SignInResponse;
import springSecurity.spring.dto.sign_up.request.SignUpRequest;
import springSecurity.spring.dto.sign_up.response.SignUpResponse;
import springSecurity.spring.entity.Member;
import springSecurity.spring.repository.MemberRepository;
import springSecurity.spring.security.TokenProvider;

@Service
@RequiredArgsConstructor
public class SignService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;
    @Transactional
    public SignUpResponse registMember(SignUpRequest request){
        Member member = memberRepository.save(Member.from(request,encoder));
        try{
            memberRepository.flush();   //강제 호출(쿼리가 DB에 반영됨)
        }catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }
        return SignUpResponse.from(member);
    }
    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        Member member = memberRepository.findByAccount(request.account())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!encoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String token = tokenProvider.createToken(String.format("%s:%s", member.getId(), member.getType()));
        return new SignInResponse(member.getName(), member.getType(), token);
    }
}
