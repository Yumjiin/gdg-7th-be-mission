package gdg.hongik.mission.controller;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.global.exception.ErrorResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// [4주차] @CrossOrigin은 WebConfig(전역 설정)로 옮겼다
@Tag(name = "관리자 API", description = "상품 등록, 재고 추가, 상품 삭제")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;

    // @Valid: 요청 DTO의 @NotBlank, @NotNull 등을 검사한다. 실패하면 Service에 들어가기 전에 400으로 응답한다
    @Operation(summary = "상품 등록",
            description = "새 상품을 등록합니다. ID는 서버가 부여하며, category는 의류·전자기기·화장품·식품 중 하나여야 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "400",
                    description = "필수 값 누락·음수 (INVALID_INPUT), category가 4가지 외의 값 (INVALID_REQUEST_BODY)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "상품명 중복 (DUPLICATE_PRODUCT_NAME)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ProductResponse> register(@RequestBody @Valid ProductCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.register(request));
    }

    @Operation(summary = "재고 추가", description = "상품명(itemName)으로 상품을 찾아 재고를 추가하고, 상품 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추가 성공"),
            @ApiResponse(responseCode = "400", description = "상품명 누락, 수량이 1 미만 (INVALID_INPUT)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음 (PRODUCT_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/stock")
    public ResponseEntity<ProductResponse> addStock(@RequestBody @Valid StockUpdateRequest request) {
        return ResponseEntity.ok(productService.addStock(request));
    }

    @Operation(summary = "상품 삭제", description = "상품명(itemName)에 해당하는 상품을 삭제하고, 남아 있는 전체 상품 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "상품명 누락 (INVALID_INPUT)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음 (PRODUCT_NOT_FOUND)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    public ResponseEntity<List<ProductResponse>> delete(@RequestBody @Valid ProductDeleteRequest request) {
        return ResponseEntity.ok(productService.delete(request));
    }
}
