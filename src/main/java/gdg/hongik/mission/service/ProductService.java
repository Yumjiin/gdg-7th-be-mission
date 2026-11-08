package gdg.hongik.mission.service;

import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 상품 관련 비즈니스 로직 (검증, 재고 계산, 가격 계산)
 * - HTTP는 모른다: 요청/응답 처리는 Controller가 담당
 * - 저장 방식은 모른다: DB 접근은 Repository가 담당
 */
@Service
@RequiredArgsConstructor   // final 필드를 받는 생성자를 만들어 줌 → Spring이 ProductRepository 빈을 주입
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 조회
    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + name));
    }

    // 상품 등록
    public Product register(String name, Integer price, Integer stock) {
        if (productRepository.existsByName(name)) {
            throw new RuntimeException("이미 존재하는 상품명입니다: " + name);
        }
        // ID는 @GeneratedValue로 DB가 부여 → 1주차의 ProductStore.sequence++ 가 필요 없어짐
        return productRepository.save(new Product(name, price, stock));
    }

    // 재고 추가
    public Product addStock(Long productId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new RuntimeException("추가 수량은 1 이상이어야 합니다.");
        }
        Product product = findById(productId);
        product.setStock(product.getStock() + quantity);
        // 주의: 트랜잭션이 없어서 이 변경은 DB에 반영되지 않을 수 있음 (3주차에서 다룰 내용)
        return product;
    }

    // 상품 구매 — 응답 형태(Map)는 3주차에 DTO로 바꿀 예정
    public Map<String, Object> order(List<Map<String, Long>> items) {
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

        // 2단계: 재고 차감 + 결과 만들기
        int totalPrice = 0;
        List<Map<String, Object>> purchased = new ArrayList<>();

        for (Map<String, Long> item : items) {
            Product product = findById(item.get("productId"));
            int quantity = item.get("quantity").intValue();
            int price = product.getPrice() * quantity;

            product.setStock(product.getStock() - quantity);   // 주의: addStock과 같은 이유로 DB 반영 안 될 수 있음
            totalPrice += price;

            Map<String, Object> line = new LinkedHashMap<>();
            line.put("name", product.getName());
            line.put("quantity", quantity);
            line.put("price", price);
            purchased.add(line);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalPrice", totalPrice);
        result.put("items", purchased);
        return result;
    }

    // 상품 삭제 — 남아 있는 상품 목록을 반환
    public List<Product> delete(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new RuntimeException("삭제할 상품 ID가 없습니다.");
        }
        // 존재하지 않는 ID가 섞여 있으면 아무것도 지우지 않고 중단
        for (Long id : productIds) {
            findById(id);
        }
        productRepository.deleteAllById(productIds);
        return productRepository.findAll();
    }

    // 1주차에 두 Controller에 중복돼 있던 헬퍼가 이제 Service 한 곳에만 있음
    private Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다. id=" + id));
    }
}