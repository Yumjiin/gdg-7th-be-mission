package gdg.hongik.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// 상품 삭제 응답 — API 명세의 { "products": [ { "name", "stock" } ] } 형태
public record RemainingProductsResponse(
        @Schema(description = "삭제 후 남아 있는 상품 목록")
        List<ProductStockResponse> products
) {
}