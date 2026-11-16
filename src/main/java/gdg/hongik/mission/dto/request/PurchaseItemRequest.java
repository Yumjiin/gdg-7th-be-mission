package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// 장바구니 구매 항목 하나 — JSON: { "id", "count" }
// 구매 요청 Body는 이 객체의 배열( [ {...}, {...} ] )이라 감싸는 DTO 없이 List로 받는다
// 배열 안 항목까지 검증하려면 Controller에서 List<@Valid PurchaseItemRequest>로 받아야 한다
public record PurchaseItemRequest(
        @JsonProperty("id")
        @NotNull(message = "상품 ID는 필수입니다")
        @Schema(description = "상품 ID", example = "1")
        Long productId,

        @JsonProperty("count")
        @NotNull(message = "구매 수량은 필수입니다")
        @Positive(message = "구매 수량은 1 이상이어야 합니다")
        @Schema(description = "구매 수량 (1 이상)", example = "2")
        Integer quantity
) {
}
