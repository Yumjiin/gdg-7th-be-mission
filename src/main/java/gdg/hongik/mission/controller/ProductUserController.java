package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.global.exception.ErrorResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// [4주차] @CrossOrigin은 WebConfig(전역 설정)로 옮겼다
@Tag(name = "소비자 API", description = "상품 조회 및 구매")
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
            @ApiResponse(responseCode = "400", description = "itemName이 비어 있음 (INVALID_INPUT)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음 (PRODUCT_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(params = "itemName")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "조회할 상품명", example = "원피스")
            @RequestParam @NotBlank(message = "상품명(itemName)은 필수입니다") String itemName) {
        return ResponseEntity.ok(productService.findByName(itemName));
    }

    // List<@Valid ...>: 배열 안의 각 항목까지 검증한다. @Valid List<...>만 쓰면 항목 안의 @NotNull, @Positive가 검사되지 않는다
    @Operation(summary = "장바구니 상품 일괄 구매",
            description = "장바구니에 담긴 상품들의 id와 구매 수량(count) 목록을 받아 재고를 차감합니다. "
                    + "같은 상품이 여러 번 들어오면 수량을 합산하며, 하나라도 재고가 부족하면 전체 구매가 취소됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구매 성공"),
            @ApiResponse(responseCode = "400", description = "빈 배열, id 누락, count가 1 미만 (INVALID_INPUT)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음 (PRODUCT_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "재고 부족 (OUT_OF_STOCK)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/purchase")
    public ResponseEntity<PurchaseResponse> purchase(
            @RequestBody
            @NotEmpty(message = "구매할 상품이 없습니다")
            List<@NotNull(message = "구매 항목이 비어 있습니다") @Valid PurchaseItemRequest> items) {
        return ResponseEntity.ok(productService.purchase(items));
    }
}
