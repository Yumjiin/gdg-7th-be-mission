package gdg.hongik.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

// 구매한 상품 한 줄 — 상품명, 구매 수량, 해당 상품 소비 금액
public record PurchasedProductResponse(
        @Schema(description = "상품명", example = "사과")
        String name,

        @Schema(description = "구매 수량", example = "2")
        Integer quantity,

        @Schema(description = "해당 상품 소비 금액 (개당 가격 × 수량)", example = "3000")
        Integer price
) {
}