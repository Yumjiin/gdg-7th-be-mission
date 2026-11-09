package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

// 상품 등록 요청 — 클라이언트가 보낼 수 있는 값은 이름, 가격, 초기 재고뿐 (id는 서버가 부여)
public record ProductCreateRequest(
        @Schema(description = "상품명 (중복 불가)", example = "사과")
        String name,

        @Schema(description = "개당 가격 (원)", example = "1500")
        Integer price,

        @Schema(description = "초기 재고 수량", example = "30")
        Integer stock
) {
}