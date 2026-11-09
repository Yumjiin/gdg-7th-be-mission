package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.PurchaseRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "소비자 API", description = "상품 조회 및 구매")
@RestController
@RequiredArgsConstructor
public class ProductUserController {

    private final ProductService productService;

    @Operation(summary = "상품 조회", description = "상품명으로 상품 정보(ID, 이름, 가격, 재고)를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "상품을 찾을 수 없음 (RuntimeException)")
    })
    @GetMapping("/products")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "조회할 상품명", example = "사과")
            @RequestParam String name) {
        return ResponseEntity.ok(productService.findByName(name));
    }

    @Operation(summary = "상품 구매", description = "여러 상품을 한 번에 구매합니다. 같은 상품이 여러 번 들어오면 수량을 합산하며, 하나라도 재고가 부족하면 전체 구매가 취소됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구매 성공"),
            @ApiResponse(responseCode = "500", description = "상품 없음, 수량 오류, 재고 부족 (RuntimeException)")
    })
    @PostMapping("/orders")
    public ResponseEntity<PurchaseResponse> order(@RequestBody PurchaseRequest request) {
        return ResponseEntity.ok(productService.order(request));
    }
}