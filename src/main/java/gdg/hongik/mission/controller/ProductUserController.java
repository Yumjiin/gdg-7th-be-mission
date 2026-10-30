package gdg.hongik.mission.controller;

import gdg.hongik.mission.Product;
import gdg.hongik.mission.ProductStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "소비자 API", description = "상품 조회 및 구매")
@RestController
public class ProductUserController {

    @Operation(summary = "상품 조회", description = "상품명으로 상품 정보(ID, 이름, 가격, 재고)를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "상품을 찾을 수 없음 (1주차는 RuntimeException)")
    })
    @GetMapping("/products")
    public ResponseEntity<Product> getProduct(
            @Parameter(description = "조회할 상품명", example = "사과")
            @RequestParam String name) {
        Product product = findByName(name);
        return ResponseEntity.ok(product);
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
            @ApiResponse(responseCode = "500", description = "상품 없음 또는 재고 부족 (1주차는 RuntimeException)")
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
        List<Map<String, Long>> items = request.get("items");
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("구매할 상품이 없습니다.");
        }

        // 1단계: 전부 검증 — 하나라도 문제가 있으면 재고를 건드리지 않고 중단
        for (Map<String, Long> item : items) {
            Product product = findById(item.get("productId"));
            int quantity = item.get("quantity").intValue();
            if (quantity < 1) {
                throw new RuntimeException("구매 수량은 1 이상이어야 합니다.");
            }
            if (product.getStock() < quantity) {
                throw new RuntimeException("재고가 부족합니다: " + product.getName());
            }
        }

        // 2단계: 재고 차감 + 응답 만들기
        int totalPrice = 0;
        List<Map<String, Object>> purchased = new ArrayList<>();

        for (Map<String, Long> item : items) {
            Product product = findById(item.get("productId"));
            int quantity = item.get("quantity").intValue();
            int price = product.getPrice() * quantity;

            product.setStock(product.getStock() - quantity);
            totalPrice += price;

            Map<String, Object> line = new LinkedHashMap<>();
            line.put("name", product.getName());
            line.put("quantity", quantity);
            line.put("price", price);
            purchased.add(line);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("totalPrice", totalPrice);
        response.put("items", purchased);
        return ResponseEntity.ok(response);
    }

    // ---- 1주차 임시 헬퍼 (2주차에 Service/Repository로 옮길 부분) ----

    private Product findByName(String name) {
        for (Product product : ProductStore.products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        throw new RuntimeException("상품을 찾을 수 없습니다: " + name);
    }

    private Product findById(Long id) {
        for (Product product : ProductStore.products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        throw new RuntimeException("상품을 찾을 수 없습니다. id=" + id);
    }
}