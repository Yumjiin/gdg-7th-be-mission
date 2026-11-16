package gdg.hongik.mission.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 서비스에서 발생할 수 있는 오류의 목록.
 *
 * <p>오류마다 HTTP 상태 코드와 기본 메시지를 한 곳에서 관리한다.
 * 새 오류가 생기면 여기에 한 줄만 추가하면 되고, 예외 처리기({@link GlobalExceptionHandler})는 고치지 않아도 된다. (OCP)
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request: 요청 자체가 잘못됨
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST,
            "요청 본문을 읽을 수 없습니다. JSON 형식, 값의 타입, category(의류·전자기기·화장품·식품)를 확인해 주세요."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "수량은 1 이상이어야 합니다."),

    // 404 Not Found: 대상이 없음
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 주소를 찾을 수 없습니다."),

    // 405, 415: HTTP 메서드, Content-Type 문제
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Content-Type은 application/json이어야 합니다."),

    // 409 Conflict: 요청 형식은 맞지만 현재 데이터 상태와 충돌함
    DUPLICATE_PRODUCT_NAME(HttpStatus.CONFLICT, "이미 존재하는 상품명입니다."),
    OUT_OF_STOCK(HttpStatus.CONFLICT, "재고가 부족합니다."),

    // 500 Internal Server Error: 예상하지 못한 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
