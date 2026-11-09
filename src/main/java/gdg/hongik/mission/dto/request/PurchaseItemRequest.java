package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// 구매 항목 하나 — 1~2주차에는 Map<String, Long> 하나로 받아서 quantity도 Long이었지만,
// 이제 필드마다 타입을 따로 정할 수 있다 (productId는 Long, quantity는 Integer)
public record PurchaseItemRequest(
        @Schema(description = "상품 ID", example = "1")
        Long productId,

        @Schema(description = "구매 수량 (1 이상)", example = "2")
        Integer quantity
) {
}