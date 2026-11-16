package gdg.hongik.mission.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gdg.hongik.mission.entity.Category;
import gdg.hongik.mission.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

// 상품 정보 응답 — JSON: { "id", "itemName", "price", "quantity", "category" }
// 전체 조회, 단건 조회, 등록, 재고 추가, 삭제 후 남은 목록에서 모두 이 모양을 쓴다
@JsonPropertyOrder({"id", "itemName", "price", "quantity", "category"}) // JSON 키 순서를 프론트 명세와 같게
public record ProductResponse(
        @Schema(description = "상품 ID (구매 시 사용)", example = "1")
        Long id,

        @JsonProperty("itemName")
        @Schema(description = "상품명", example = "원피스")
        String name,

        @Schema(description = "개당 가격 (원)", example = "1000")
        Integer price,

        @JsonProperty("quantity")
        @Schema(description = "재고 수량", example = "13")
        Integer stock,

        @Schema(description = "카테고리", example = "의류")
        Category category
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(), product.getName(), product.getPrice(), product.getStock(), product.getCategory());
    }
}