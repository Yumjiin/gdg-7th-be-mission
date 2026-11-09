package gdg.hongik.mission.dto.response;

import gdg.hongik.mission.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

// 상품명 + 재고만 담는 응답 — 재고 추가 결과, 삭제 후 남은 상품 목록에서 사용
public record ProductStockResponse(
        @Schema(description = "상품명", example = "사과")
        String name,

        @Schema(description = "재고 수량", example = "50")
        Integer stock
) {
    public static ProductStockResponse from(Product product) {
        return new ProductStockResponse(product.getName(), product.getStock());
    }
}