package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

// 재고 추가 요청 — JSON: { "itemName", "quantity" }
// 상품을 ID가 아니라 상품명으로 찾는다 (프론트 관리자 화면에서 상품명을 입력)
public record StockUpdateRequest(
        @JsonProperty("itemName")
        @Schema(description = "재고를 추가할 상품명", example = "원피스")
        String name,

        @Schema(description = "추가할 수량 (1 이상)", example = "5")
        Integer quantity
) {
}