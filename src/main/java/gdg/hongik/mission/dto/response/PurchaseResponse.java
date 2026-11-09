package gdg.hongik.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// 구매 응답 — 총 구매 금액 + 구매한 상품 목록
public record PurchaseResponse(
        @Schema(description = "총 구매 금액 (원)", example = "5000")
        Integer totalPrice,

        @Schema(description = "구매한 상품 목록")
        List<PurchasedProductResponse> items
) {
}