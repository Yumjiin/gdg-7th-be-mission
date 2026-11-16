package gdg.hongik.mission.global.exception;

import lombok.Getter;

/**
 * 비즈니스 규칙을 어겼을 때 던지는 예외. (상품 없음, 상품명 중복, 재고 부족 등)
 *
 * <p>{@link RuntimeException}(언체크 예외)을 상속해서
 * <ul>
 *   <li>메서드마다 {@code throws}를 선언하지 않아도 되고,</li>
 *   <li>{@code @Transactional} 메서드 안에서 던지면 기본 규칙에 따라 롤백된다.</li>
 * </ul>
 * 어떤 오류인지는 {@link ErrorCode}로 구분하므로, 오류마다 예외 클래스를 따로 만들지 않는다.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /** {@link ErrorCode}의 기본 메시지를 그대로 쓴다. */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /** 기본 메시지 대신 상황에 맞는 메시지를 쓴다. 예: "재고가 부족합니다: 원피스" */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
