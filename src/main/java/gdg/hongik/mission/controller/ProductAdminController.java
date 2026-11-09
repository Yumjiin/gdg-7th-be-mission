package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.ProductStockResponse;
import gdg.hongik.mission.dto.response.RemainingProductsResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 API", description = "상품 등록, 재고 추가, 상품 삭제")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "새 상품을 등록합니다. ID는 서버가 부여하며, 이름이 비어 있거나 같은 이름의 상품이 있으면 실패합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "500", description = "입력값 오류 또는 상품명 중복 (RuntimeException)")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> register(@RequestBody ProductCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.register(request));
    }

    @Operation(summary = "재고 추가", description = "기존 상품의 재고를 추가하고 상품명과 최종 재고를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추가 성공"),
            @ApiResponse(responseCode = "500", description = "상품 없음 또는 수량 오류 (RuntimeException)")
    })
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<ProductStockResponse> addStock(
            @Parameter(description = "재고를 추가할 상품 ID", example = "1")
            @PathVariable Long productId,
            @RequestBody StockUpdateRequest request) {
        return ResponseEntity.ok(productService.addStock(productId, request));
    }

    @Operation(summary = "상품 삭제", description = "여러 상품을 한 번에 삭제하고, 남아 있는 상품 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "존재하지 않는 ID 포함 (RuntimeException)")
    })
    @DeleteMapping
    public ResponseEntity<RemainingProductsResponse> delete(@RequestBody ProductDeleteRequest request) {
        return ResponseEntity.ok(productService.delete(request));
    }
}