package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// 상품 삭제 요청 — 여러 상품을 한 번에 삭제
public record ProductDeleteRequest(
        @Schema(description = "삭제할 상품 ID 목록", example = "[1, 3]")
        List<Long> productIds
) {
}