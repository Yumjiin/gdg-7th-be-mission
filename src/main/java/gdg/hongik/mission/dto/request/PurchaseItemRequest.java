package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

// 장바구니 구매 항목 하나 — JSON: { "id", "count" }
// 구매 요청 Body는 이 객체의 배열( [ {...}, {...} ] )이라 감싸는 DTO 없이 List로 받는다
public record PurchaseItemRequest(
        @JsonProperty("id")
        @Schema(description = "상품 ID", example = "1")
        Long productId,

        @JsonProperty("count")
        @Schema(description = "구매 수량 (1 이상)", example = "2")
        Integer quantity
) {
}