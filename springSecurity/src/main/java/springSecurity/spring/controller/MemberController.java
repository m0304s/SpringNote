package springSecurity.spring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springSecurity.spring.annotation.UserAuthorize;
import springSecurity.spring.dto.ApiResponse;
import springSecurity.spring.dto.member.request.MemberUpdateRequest;
import springSecurity.spring.service.MemberService;

import java.util.UUID;

@Tag(name = "로그인 후 사용할 수 있는 API")
@UserAuthorize
@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    @Operation(summary = "회원 정보 조회")
    @GetMapping
    public ApiResponse getMemberInfo(String id){
        return ApiResponse.success(memberService.getMemberInfo(UUID.fromString(id)));
    }
    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public ApiResponse deleteMember(String id){
        return ApiResponse.success(memberService.deleteMember(UUID.fromString(id)));
    }
    @Operation(summary = "회원 정보 수정")
    @PostMapping
    public ApiResponse updateMember(String id, @RequestBody MemberUpdateRequest request){
        return ApiResponse.success(memberService.updateMember(UUID.fromString(id),request));
    }
}
