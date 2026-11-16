package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// 재고 추가 요청 — JSON: { "itemName", "quantity" }
// 상품을 ID가 아니라 상품명으로 찾는다 (프론트 관리자 화면에서 상품명을 입력)
public record StockUpdateRequest(
        @JsonProperty("itemName")
        @NotBlank(message = "상품명은 필수입니다")
        @Schema(description = "재고를 추가할 상품명", example = "원피스")
        String name,

        @NotNull(message = "추가 수량은 필수입니다")
        @Positive(message = "추가 수량은 1 이상이어야 합니다")
        @Schema(description = "추가할 수량 (1 이상)", example = "5")
        Integer quantity
) {
}
