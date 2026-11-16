package gdg.hongik.mission.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

// 상품 삭제 요청 — JSON: { "itemName" } (상품명 기준으로 한 개 삭제)
public record ProductDeleteRequest(
        @JsonProperty("itemName")
        @Schema(description = "삭제할 상품명", example = "원피스")
        String name
) {
}