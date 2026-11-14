package gdg.hongik.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// 구매 응답 — JSON: { "totalPrice", "items": [ ... ] }
public record PurchaseResponse(
        @Schema(description = "전체 구매 총액 (원)", example = "2200")
        Integer totalPrice,

        @Schema(description = "구매 완료된 상품 목록")
        List<PurchasedProductResponse> items
) {
}