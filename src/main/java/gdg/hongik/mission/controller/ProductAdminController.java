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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "관리자 API", description = "상품 등록, 재고 추가, 상품 삭제")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "새 상품을 등록합니다. ID는 서버가 부여하며, 같은 이름의 상품이 있으면 실패합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "500", description = "상품명 중복 (RuntimeException)")
    })
    @PostMapping
    public ResponseEntity<Product> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "상품명, 개당 가격, 초기 재고 수량",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            { "name": "사과", "price": 1500, "stock": 30 }
                            """)))
            @RequestBody Map<String, Object> request) {
        Product product = productService.register(
                (String) request.get("name"),
                ((Number) request.get("price")).intValue(),
                ((Number) request.get("stock")).intValue()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Operation(summary = "재고 추가", description = "기존 상품의 재고를 추가하고 상품명과 최종 재고를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추가 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            { "name": "사과", "stock": 50 }
                            """))),
            @ApiResponse(responseCode = "500", description = "상품 없음 또는 수량 오류 (RuntimeException)")
    })
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Map<String, Object>> addStock(
            @Parameter(description = "재고를 추가할 상품 ID", example = "1")
            @PathVariable Long productId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "추가할 수량",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            { "quantity": 20 }
                            """)))
            @RequestBody Map<String, Integer> request) {
        Product product = productService.addStock(productId, request.get("quantity"));

        // 응답 모양 만들기는 Controller의 일 (3주차에 DTO로 대체)
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("name", product.getName());
        response.put("stock", product.getStock());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 삭제", description = "여러 상품을 한 번에 삭제하고, 남아 있는 상품 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            { "products": [ { "name": "바나나", "stock": 10 } ] }
                            """))),
            @ApiResponse(responseCode = "500", description = "존재하지 않는 ID 포함 (RuntimeException)")
    })
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> delete(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "삭제할 상품 ID 목록",
                    content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                            { "productIds": [1, 3] }
                            """)))
            @RequestBody Map<String, List<Long>> request) {
        List<Product> remainingProducts = productService.delete(request.get("productIds"));

        List<Map<String, Object>> remaining = new ArrayList<>();
        for (Product product : remainingProducts) {
            Map<String, Object> line = new LinkedHashMap<>();
            line.put("name", product.getName());
            line.put("stock", product.getStock());
            remaining.add(line);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("products", remaining);
        return ResponseEntity.ok(response);
    }
}