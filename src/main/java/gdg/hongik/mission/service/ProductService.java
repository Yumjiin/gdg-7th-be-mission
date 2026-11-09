package gdg.hongik.mission.service;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.request.PurchaseRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.ProductStockResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.dto.response.PurchasedProductResponse;
import gdg.hongik.mission.dto.response.RemainingProductsResponse;
import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 상품 관련 비즈니스 로직
 * - 요청 DTO를 받아 검증·계산하고, 응답 DTO로 돌려준다
 * - Entity는 Service 밖(Controller)으로 나가지 않는다
 * - 기본은 읽기 전용 트랜잭션, 데이터를 바꾸는 메서드만 쓰기 트랜잭션
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 조회
    public ProductResponse findByName(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + name));
        return ProductResponse.from(product);
    }

    // 상품 등록
    @Transactional
    public ProductResponse register(ProductCreateRequest request) {
        // [1주차 리뷰 반영] name·price·stock 누락 또는 음수면 저장 전에 막는다
        if (request.name() == null || request.name().isBlank()) {
            throw new RuntimeException("상품명은 필수입니다.");
        }
        if (request.price() == null || request.price() < 0) {
            throw new RuntimeException("가격은 0 이상이어야 합니다.");
        }
        if (request.stock() == null || request.stock() < 0) {
            throw new RuntimeException("재고는 0 이상이어야 합니다.");
        }
        if (productRepository.existsByName(request.name())) {
            throw new RuntimeException("이미 존재하는 상품명입니다: " + request.name());
        }

        Product saved = productRepository.save(new Product(request.name(), request.price(), request.stock()));
        return ProductResponse.from(saved);
    }

    // 재고 추가 — save() 없이 값만 바꿔도, 트랜잭션이 끝날 때 더티 체킹으로 UPDATE가 나간다
    @Transactional
    public ProductStockResponse addStock(Long productId, StockUpdateRequest request) {
        if (request.quantity() == null || request.quantity() < 1) {
            throw new RuntimeException("추가 수량은 1 이상이어야 합니다.");
        }
        Product product = findById(productId);
        product.setStock(product.getStock() + request.quantity());
        return ProductStockResponse.from(product);
    }

    // 상품 구매 — 중간에 예외가 나면 트랜잭션 전체가 롤백된다
    @Transactional
    public PurchaseResponse order(PurchaseRequest request) {
        List<PurchaseItemRequest> items = request.items();
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("구매할 상품이 없습니다.");
        }

        // 0단계: 입력 검증 + 상품별 수량 합산
        // [1주차 리뷰 반영] 같은 productId가 여러 번 들어와도 합계로 재고를 검증한다
        Map<Long, Integer> quantityByProductId = new LinkedHashMap<>();
        for (PurchaseItemRequest item : items) {
            if (item.productId() == null) {
                throw new RuntimeException("상품 ID는 필수입니다.");
            }
            // [1주차 리뷰 반영] quantity 누락 시 NPE 대신 명확한 메시지
            if (item.quantity() == null || item.quantity() < 1) {
                throw new RuntimeException("구매 수량은 1 이상이어야 합니다.");
            }
            quantityByProductId.merge(item.productId(), item.quantity(), Integer::sum);
        }

        // 1단계: 전부 검증 — 하나라도 문제가 있으면 재고를 건드리지 않고 중단
        Map<Product, Integer> checked = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : quantityByProductId.entrySet()) {
            Product product = findById(entry.getKey());
            int quantity = entry.getValue();
            if (product.getStock() < quantity) {
                throw new RuntimeException("재고가 부족합니다: " + product.getName());
            }
            checked.put(product, quantity);
        }

        // 2단계: 재고 차감 + 응답 만들기
        int totalPrice = 0;
        List<PurchasedProductResponse> purchased = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : checked.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            int price = product.getPrice() * quantity;

            product.setStock(product.getStock() - quantity);
            totalPrice += price;
            purchased.add(new PurchasedProductResponse(product.getName(), quantity, price));
        }

        return new PurchaseResponse(totalPrice, purchased);
    }

    // 상품 삭제 — 남아 있는 상품 목록을 반환
    @Transactional
    public RemainingProductsResponse delete(ProductDeleteRequest request) {
        List<Long> productIds = request.productIds();
        if (productIds == null || productIds.isEmpty()) {
            throw new RuntimeException("삭제할 상품 ID가 없습니다.");
        }
        // 존재하지 않는 ID가 섞여 있으면 아무것도 지우지 않고 중단
        for (Long id : productIds) {
            findById(id);
        }
        productRepository.deleteAllById(productIds);

        List<ProductStockResponse> remaining = productRepository.findAll().stream()
                .map(ProductStockResponse::from)
                .toList();
        return new RemainingProductsResponse(remaining);
    }

    private Product findById(Long id) {
        if (id == null) {
            throw new RuntimeException("상품 ID는 필수입니다.");
        }
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id=" + id));
    }
}