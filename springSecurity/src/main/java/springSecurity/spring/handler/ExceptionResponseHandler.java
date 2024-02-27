package springSecurity.spring.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springSecurity.spring.dto.ApiResponse;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionResponseHandler {
    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(Exception e){
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("접근이 거부되었습니다."));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(){
        return ResponseEntity.internalServerError().body(ApiResponse.error("서버에 문제가 발생하였습니다."));
    }
    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse> handleSignatureException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 유효하지 않습니다."));
    }
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse> handleMalformedJwtException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("올바르지 않은 토큰입니다."));
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 만료되었습니다. 다시 로그인해주세요"));
    }
}
