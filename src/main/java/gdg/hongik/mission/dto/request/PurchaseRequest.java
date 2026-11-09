package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

// 구매 요청 전체 — API 명세의 { "items": [ ... ] } 형태를 그대로 받는다
public record PurchaseRequest(
        @Schema(description = "구매할 상품 목록 (1개 이상)")
        List<PurchaseItemRequest> items
) {
}