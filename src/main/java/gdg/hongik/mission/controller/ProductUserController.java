package gdg.hongik.mission.controller;

import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "소비자 API", description = "상품 조회 및 구매")
@RestController
@RequiredArgsConstructor
public class ProductUserController {

    private final ProductService productService;   // new 하지 않음 — Spring이 빈을 주입

    @Operation(summary = "상품 조회", description = "상품명으로 상품 정보(ID, 이름, 가격, 재고)를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "상품을 찾을 수 없음 (RuntimeException)")
    })
    @GetMapping("/products")
    public ResponseEntity<Product> getProduct(
            @Parameter(description = "조회할 상품명", example = "사과")
            @RequestParam String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @Operation(summary = "상품 구매", description = "여러 상품을 한 번에 구매합니다. 하나라도 재고가 부족하면 전체 구매가 취소됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구매 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "totalPrice": 5000,
                              "items": [
                                { "name": "사과", "quantity": 2, "price": 3000 },
                                { "name": "바나나", "quantity": 1, "price": 2000 }
                              ]
                            }
                            """))),
            @ApiResponse(responseCode = "500", description = "상품 없음 또는 재고 부족 (RuntimeException)")
    })
    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> order(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "구매할 상품 ID와 수량 목록",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            {
                              "items": [
                                { "productId": 1, "quantity": 2 },
                                { "productId": 2, "quantity": 1 }
                              ]
                            }
                            """)))
            @RequestBody Map<String, List<Map<String, Long>>> request) {
        return ResponseEntity.ok(productService.order(request.get("items")));
    }
}