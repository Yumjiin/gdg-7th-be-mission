package gdg.hongik.mission;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

// Swagger 페이지 상단에 표시될 문서 제목과 설명
@OpenAPIDefinition(info = @Info(
        title = "GDG 쇼핑몰 API",
        description = "GDG on Campus 홍익대 백엔드 미션코스 1주차 — 상품 조회·구매(소비자), 상품 등록·재고 관리(관리자)",
        version = "v1"
))
@Configuration
public class SwaggerConfig {
}