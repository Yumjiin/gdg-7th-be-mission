package gdg.hongik.mission.dto.response;

import gdg.hongik.mission.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

// 상품 정보 응답 — 상품 조회, 상품 등록에서 사용
public record ProductResponse(
        @Schema(description = "상품 ID (구매 시 사용)", example = "1")
        Long id,

        @Schema(description = "상품명", example = "사과")
        String name,

        @Schema(description = "개당 가격 (원)", example = "1500")
        Integer price,

        @Schema(description = "재고 수량", example = "30")
        Integer stock
) {
    // Entity → DTO 변환을 한 곳에 모아 둔다
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock());
    }
}