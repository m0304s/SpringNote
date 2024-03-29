package springSecurity.spring.dto.sign_in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import springSecurity.spring.common.MemberType;

public record SignInResponse(
        @Schema(description = "회원 이름",example = "콜라곰")
        String name,
        @Schema(description = "회원 유형",example = "USER")
        MemberType type,
        String AccessToken,
        String refreshToken
) {
}
