package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import gdg.hongik.mission.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

// 상품 등록 요청 — JSON: { "itemName", "price", "quantity", "category" }
// 자바 코드에서는 기존 이름(name, stock)을 그대로 쓰고, JSON 키만 프론트 명세에 맞춘다
public record ProductCreateRequest(
        @JsonProperty("itemName")
        @Schema(description = "상품명 (중복 불가)", example = "원피스")
        String name,

        @Schema(description = "개당 가격 (원)", example = "1000")
        Integer price,

        @JsonProperty("quantity")
        @Schema(description = "초기 재고 수량", example = "13")
        Integer stock,

        @Schema(description = "카테고리 (의류, 전자기기, 화장품, 식품 중 하나)", example = "의류")
        Category category
) {
}