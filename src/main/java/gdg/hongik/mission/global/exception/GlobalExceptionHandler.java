package gdg.hongik.mission.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Objects;

/**
 * 모든 컨트롤러에서 발생한 예외를 한 곳에서 받아 {@link ErrorResponse} 형식으로 바꾼다.
 *
 * <p>컨트롤러와 서비스는 예외를 던지기만 하고, 어떤 상태 코드와 JSON으로 응답할지는 이 클래스가 정한다. (SRP)
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 비즈니스 규칙 위반: 상품 없음(404), 상품명 중복(409), 재고 부족(409) 등. */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return toResponse(errorCode, e.getMessage());
    }

    /** {@code @Valid @RequestBody} DTO 검증 실패. 예: 상품 등록 시 itemName이 비어 있음. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<String> messages = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return toResponse(ErrorCode.INVALID_INPUT, joinMessages(messages));
    }

    /**
     * 컨트롤러 메서드 파라미터 검증 실패.
     * 예: 구매 요청 배열이 비어 있음({@code @NotEmpty}), 배열 안 항목의 count가 0({@code List<@Valid ...>}),
     * 단건 조회의 itemName이 빈 문자열({@code @NotBlank @RequestParam}).
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidation(HandlerMethodValidationException e) {
        List<String> messages = e.getAllErrors().stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();
        return toResponse(ErrorCode.INVALID_INPUT, joinMessages(messages));
    }

    /** JSON 문법 오류, 숫자 칸에 문자열, category에 없는 값("가방") 등 요청 본문을 객체로 바꾸지 못한 경우. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return toResponse(ErrorCode.INVALID_REQUEST_BODY, ErrorCode.INVALID_REQUEST_BODY.getMessage());
    }

    /** 필수 쿼리 파라미터 누락. 예: {@code GET /products?itemName}이 아니라 다른 이름으로 보냄. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException e) {
        return toResponse(ErrorCode.INVALID_INPUT, e.getParameterName() + " 파라미터가 필요합니다.");
    }

    /** 없는 주소로 요청. 아래 {@link #handleException}이 500으로 바꾸지 않도록 따로 처리한다. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException e) {
        return toResponse(ErrorCode.API_NOT_FOUND, ErrorCode.API_NOT_FOUND.getMessage());
    }

    /** 주소는 맞지만 HTTP 메서드가 다름. 예: 재고 추가를 PATCH가 아니라 POST로 보냄. */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return toResponse(ErrorCode.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED.getMessage());
    }

    /** 본문을 보내면서 Content-Type을 application/json으로 지정하지 않음. */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        return toResponse(ErrorCode.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessage());
    }

    /**
     * 위에서 처리하지 못한 모든 예외. 예상하지 못한 서버 오류이므로 500으로 응답한다.
     * 내부 정보(SQL, 스택 트레이스)는 응답에 넣지 않고 서버 로그에만 남긴다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("처리하지 못한 예외", e);
        return toResponse(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    // ---- 내부 헬퍼 ----

    private ResponseEntity<ErrorResponse> toResponse(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode, message));
    }

    /** 검증 오류가 여러 개면 메시지를 ", "로 이어 붙인다. 예: "상품명은 필수입니다, 가격은 0 이상이어야 합니다" */
    private String joinMessages(List<String> messages) {
        List<String> distinct = messages.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (distinct.isEmpty()) {
            return ErrorCode.INVALID_INPUT.getMessage();
        }
        return String.join(", ", distinct);
    }
}
