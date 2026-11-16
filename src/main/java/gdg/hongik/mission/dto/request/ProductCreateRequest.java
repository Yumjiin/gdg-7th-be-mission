package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import gdg.hongik.mission.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// 상품 등록 요청 — JSON: { "itemName", "price", "quantity", "category" }
// 자바 코드에서는 기존 이름(name, stock)을 그대로 쓰고, JSON 키만 프론트 명세에 맞춘다
// [4주차] Service의 if문 검증을 Bean Validation 어노테이션으로 옮겼다. Controller의 @Valid가 검증을 실행한다
public record ProductCreateRequest(
        @JsonProperty("itemName")
        @NotBlank(message = "상품명은 필수입니다")
        @Size(max = 50, message = "상품명은 50자 이하여야 합니다")
        @Schema(description = "상품명 (중복 불가, 50자 이하)", example = "원피스")
        String name,

        @NotNull(message = "가격은 필수입니다")
        @PositiveOrZero(message = "가격은 0 이상이어야 합니다")
        @Schema(description = "개당 가격 (원, 0 이상)", example = "1000")
        Integer price,

        @JsonProperty("quantity")
        @NotNull(message = "재고 수량은 필수입니다")
        @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다")
        @Schema(description = "초기 재고 수량 (0 이상)", example = "13")
        Integer stock,

        @NotNull(message = "카테고리는 필수입니다 (의류, 전자기기, 화장품, 식품 중 하나)")
        @Schema(description = "카테고리 (의류, 전자기기, 화장품, 식품 중 하나)", example = "의류")
        Category category
) {
}
