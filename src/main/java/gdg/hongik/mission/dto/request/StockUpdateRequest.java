package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// 재고 추가 요청 — 상품 ID는 경로(/admin/products/{productId}/stock)로 받으므로 수량만 담는다
public record StockUpdateRequest(
        @Schema(description = "추가할 수량 (1 이상)", example = "20")
        Integer quantity
) {
}