package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "관리자 API", description = "상품 등록, 재고 추가, 상품 삭제")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    @Operation(summary = "상품 등록",
            description = "새 상품을 등록합니다. ID는 서버가 부여하며, category는 의류·전자기기·화장품·식품 중 하나여야 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "category 값이 4가지 중 하나가 아님 (JSON 변환 실패)"),
            @ApiResponse(responseCode = "500", description = "입력값 누락·음수 또는 상품명 중복 (RuntimeException)")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> register(@RequestBody ProductCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.register(request));
    }

    @Operation(summary = "재고 추가", description = "상품명(itemName)으로 상품을 찾아 재고를 추가하고, 상품 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추가 성공"),
            @ApiResponse(responseCode = "500", description = "상품 없음 또는 수량 오류 (RuntimeException)")
    })
    @PatchMapping("/stock")
    public ResponseEntity<ProductResponse> addStock(@RequestBody StockUpdateRequest request) {
        return ResponseEntity.ok(productService.addStock(request));
    }

    @Operation(summary = "상품 삭제", description = "상품명(itemName)에 해당하는 상품을 삭제하고, 남아 있는 전체 상품 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "상품 없음 (RuntimeException)")
    })
    @DeleteMapping
    public ResponseEntity<List<ProductResponse>> delete(@RequestBody ProductDeleteRequest request) {
        return ResponseEntity.ok(productService.delete(request));
    }
}