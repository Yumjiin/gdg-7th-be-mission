package gdg.hongik.mission.service;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.dto.response.PurchasedProductResponse;
import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.global.exception.BusinessException;
import gdg.hongik.mission.global.exception.ErrorCode;
import gdg.hongik.mission.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 상품 관련 비즈니스 로직.
 *
 * <ul>
 *   <li>입력값 형식 검증(필수 값, 범위)은 요청 DTO의 Bean Validation이 맡는다.</li>
 *   <li>재고 규칙(음수 불가, 재고 부족)은 {@link Product} 엔티티가 맡는다.</li>
 *   <li>이 클래스는 데이터 상태에 따른 규칙(상품 존재 여부, 상품명 중복)과 흐름 조립을 맡는다.</li>
 * </ul>
 * 기본은 읽기 전용 트랜잭션이고, 데이터를 바꾸는 메서드에만 쓰기 트랜잭션을 건다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 전체 상품 목록을 조회한다.
     *
     * @return 전체 상품 목록 (없으면 빈 목록)
     */
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    /**
     * 상품명으로 상품 하나를 조회한다.
     *
     * @param name 상품명
     * @return 상품 정보
     * @throws BusinessException 상품이 없으면 {@link ErrorCode#PRODUCT_NOT_FOUND}
     */
    public ProductResponse findByName(String name) {
        return ProductResponse.from(findEntityByName(name));
    }

    /**
     * 상품을 등록한다.
     *
     * @param request 상품명, 가격, 재고, 카테고리 (형식 검증은 Controller의 {@code @Valid}에서 끝난 상태)
     * @return 등록된 상품 정보 (서버가 부여한 ID 포함)
     * @throws BusinessException 같은 이름의 상품이 있으면 {@link ErrorCode#DUPLICATE_PRODUCT_NAME}
     */
    @Transactional
    public ProductResponse register(ProductCreateRequest request) {
        if (productRepository.existsByName(request.name())) {
            throw new BusinessException(ErrorCode.DUPLICATE_PRODUCT_NAME,
                    "이미 존재하는 상품명입니다: " + request.name());
        }
        Product saved = productRepository.save(
                new Product(request.name(), request.price(), request.stock(), request.category()));
        return ProductResponse.from(saved);
    }

    /**
     * 상품명으로 상품을 찾아 재고를 추가한다.
     * {@code save()}를 호출하지 않아도 트랜잭션이 끝날 때 더티 체킹으로 UPDATE가 나간다.
     *
     * @param request 상품명, 추가 수량
     * @return 재고가 추가된 상품 정보
     * @throws BusinessException 상품이 없으면 {@link ErrorCode#PRODUCT_NOT_FOUND}
     */
    @Transactional
    public ProductResponse addStock(StockUpdateRequest request) {
        Product product = findEntityByName(request.name());
        product.addStock(request.quantity());
        return ProductResponse.from(product);
    }

    /**
     * 장바구니 상품을 일괄 구매한다.
     *
     * <p>하나라도 실패하면 예외가 트랜잭션 밖으로 나가면서 전체가 롤백된다.
     * 재고 차감은 커밋 시점에야 UPDATE로 DB에 반영되므로, 중간에 실패해도 DB는 바뀌지 않는다.
     *
     * @param items 구매할 상품 ID와 수량 목록 (같은 ID가 여러 번 들어오면 수량을 합산)
     * @return 총 구매 금액과 구매한 상품 목록
     * @throws BusinessException 상품이 없으면 {@link ErrorCode#PRODUCT_NOT_FOUND},
     *                           재고가 부족하면 {@link ErrorCode#OUT_OF_STOCK}
     */
    @Transactional
    public PurchaseResponse purchase(List<PurchaseItemRequest> items) {
        Map<Long, Integer> quantityByProductId = mergeQuantities(items);

        List<PurchasedProductResponse> purchased = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : quantityByProductId.entrySet()) {
            purchased.add(purchaseOne(entry.getKey(), entry.getValue()));
        }

        int totalPrice = purchased.stream()
                .mapToInt(PurchasedProductResponse::price)
                .sum();
        return new PurchaseResponse(totalPrice, purchased);
    }

    /**
     * 상품명으로 상품을 삭제한다.
     *
     * @param request 삭제할 상품명
     * @return 삭제 후 남아 있는 전체 상품 목록
     * @throws BusinessException 상품이 없으면 {@link ErrorCode#PRODUCT_NOT_FOUND}
     */
    @Transactional
    public List<ProductResponse> delete(ProductDeleteRequest request) {
        Product product = findEntityByName(request.name());
        productRepository.delete(product);
        return findAll();
    }

    // ---- 내부 헬퍼 ----

    /** 같은 상품 ID가 여러 번 들어와도 합계로 재고를 확인하도록 수량을 합친다. 요청 순서는 유지한다. */
    private Map<Long, Integer> mergeQuantities(List<PurchaseItemRequest> items) {
        Map<Long, Integer> quantityByProductId = new LinkedHashMap<>();
        for (PurchaseItemRequest item : items) {
            quantityByProductId.merge(item.productId(), item.quantity(), Integer::sum);
        }
        return quantityByProductId;
    }

    /** 상품 하나의 재고를 차감하고, 응답에 들어갈 한 줄을 만든다. */
    private PurchasedProductResponse purchaseOne(Long productId, int quantity) {
        Product product = findEntityById(productId);
        product.decreaseStock(quantity); // 재고 부족이면 여기서 예외
        return new PurchasedProductResponse(product.getName(), quantity, product.calculatePrice(quantity));
    }

    private Product findEntityByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND,
                        "상품을 찾을 수 없습니다: " + name));
    }

    private Product findEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND,
                        "상품을 찾을 수 없습니다. id=" + id));
    }
}
