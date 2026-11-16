package gdg.hongik.mission.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 상품 카테고리 — 들어올 수 있는 값이 4개로 정해져 있어서 String 대신 enum으로 제한한다
@Getter
@RequiredArgsConstructor
public enum Category {
    CLOTHES("의류"),
    ELECTRONICS("전자기기"),
    COSMETICS("화장품"),
    FOOD("식품");

    @JsonValue // 응답 시 CLOTHES → "의류", 요청 시 "의류" → CLOTHES 로 양방향 변환
    private final String label;
}