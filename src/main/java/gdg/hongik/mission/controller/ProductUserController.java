package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.PurchaseItemRequest;
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

import java.util.List;

@Tag(name = "소비자 API", description = "상품 조회 및 구매")
@CrossOrigin(origins = "*") // 모든 출처(프론트엔드)에서의 API 요청을 허용
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductUserController {

    private final ProductService productService;

    @Operation(summary = "전체 상품 목록 조회", description = "등록된 전체 상품 리스트를 조회합니다. (홈 화면 첫 진입)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (상품이 없으면 빈 배열)")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    // params = "itemName": 쿼리 파라미터에 itemName이 있을 때만 이 메서드가 실행된다 (전체 조회와 경로 충돌 방지)
    @Operation(summary = "상품명 단건 검색", description = "상품명으로 상품 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "500", description = "상품을 찾을 수 없음 (RuntimeException)")
    })
    @GetMapping(params = "itemName")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "조회할 상품명", example = "원피스")
            @RequestParam String itemName) {
        return ResponseEntity.ok(productService.findByName(itemName));
    }

    @Operation(summary = "장바구니 상품 일괄 구매",
            description = "장바구니에 담긴 상품들의 id와 구매 수량(count) 목록을 받아 재고를 차감합니다. 같은 상품이 여러 번 들어오면 수량을 합산하며, 하나라도 재고가 부족하면 전체 구매가 취소됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구매 성공"),
            @ApiResponse(responseCode = "500", description = "상품 없음, 수량 오류, 재고 부족 (RuntimeException)")
    })
    @PostMapping("/purchase")
    public ResponseEntity<PurchaseResponse> purchase(@RequestBody List<PurchaseItemRequest> items) {
        return ResponseEntity.ok(productService.purchase(items));
    }
}