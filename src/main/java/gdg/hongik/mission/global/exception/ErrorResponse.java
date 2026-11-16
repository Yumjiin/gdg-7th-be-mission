package gdg.hongik.mission.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 모든 오류 응답이 공통으로 쓰는 JSON 형식.
 *
 * <pre>
 * {
 *   "status": 404,
 *   "code": "PRODUCT_NOT_FOUND",
 *   "message": "상품을 찾을 수 없습니다: 없는상품"
 * }
 * </pre>
 *
 * @param status  HTTP 상태 코드
 * @param code    오류 종류. 프론트엔드가 분기 처리에 쓰는 고정 문자열
 * @param message 사용자에게 보여 줄 수 있는 설명
 */
public record ErrorResponse(
        @Schema(description = "HTTP 상태 코드", example = "404")
        int status,

        @Schema(description = "오류 코드 (분기 처리용 고정 값)", example = "PRODUCT_NOT_FOUND")
        String code,

        @Schema(description = "오류 메시지", example = "상품을 찾을 수 없습니다: 없는상품")
        String message
) {
    /** {@link ErrorCode}의 기본 메시지로 응답을 만든다. */
    public static ErrorResponse of(ErrorCode errorCode) {
        return of(errorCode, errorCode.getMessage());
    }

    /** {@link ErrorCode}의 상태 코드와 이름에, 상황에 맞는 메시지를 붙여 응답을 만든다. */
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.getStatus().value(), errorCode.name(), message);
    }
}
