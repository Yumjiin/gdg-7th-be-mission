package gdg.hongik.mission.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

// 구매한 상품 한 줄 — JSON: { "itemName", "count", "price" }
@JsonPropertyOrder({"itemName", "count", "price"})
public record PurchasedProductResponse(
        @JsonProperty("itemName")
        @Schema(description = "상품명", example = "원피스")
        String name,

        @JsonProperty("count")
        @Schema(description = "구매 수량", example = "2")
        Integer quantity,

        @Schema(description = "해당 상품 소비 금액 (단가 × 구매 수량)", example = "2000")
        Integer price
) {
}