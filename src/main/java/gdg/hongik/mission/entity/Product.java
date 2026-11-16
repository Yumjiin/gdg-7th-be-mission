package gdg.hongik.mission.entity;

import gdg.hongik.mission.global.exception.BusinessException;
import gdg.hongik.mission.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 엔티티.
 *
 * <p>3주차까지는 {@code @Setter}로 재고를 바깥에서 마음대로 바꿀 수 있었고, "재고는 음수가 될 수 없다" 같은 규칙이
 * Service 곳곳에 흩어져 있었다. 이제 재고는 {@link #addStock(int)}, {@link #decreaseStock(int)}로만 바꿀 수 있고,
 * 규칙도 이 클래스 안에서 검사한다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // 상품명으로 상품을 찾으므로 중복을 DB에서도 막는다
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING) // 반드시 STRING! 생략하면 ORDINAL(0, 1, 2 순번)로 저장된다
    @Column(nullable = false)
    private Category category;

    public Product(String name, Integer price, Integer stock, Category category) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    /**
     * 재고를 늘린다.
     *
     * @param quantity 추가할 수량 (1 이상)
     * @throws BusinessException 수량이 1 미만이면 {@link ErrorCode#INVALID_QUANTITY}
     */
    public void addStock(int quantity) {
        validateQuantity(quantity);
        this.stock += quantity;
    }

    /**
     * 재고를 줄인다. (구매)
     *
     * @param quantity 차감할 수량 (1 이상)
     * @throws BusinessException 수량이 1 미만이면 {@link ErrorCode#INVALID_QUANTITY},
     *                           재고가 부족하면 {@link ErrorCode#OUT_OF_STOCK}
     */
    public void decreaseStock(int quantity) {
        validateQuantity(quantity);
        if (this.stock < quantity) {
            throw new BusinessException(ErrorCode.OUT_OF_STOCK,
                    "재고가 부족합니다: " + name + " (남은 재고 " + stock + "개, 요청 " + quantity + "개)");
        }
        this.stock -= quantity;
    }

    /**
     * 구매 수량에 대한 금액을 계산한다.
     *
     * @param quantity 구매 수량
     * @return 개당 가격 × 수량
     */
    public int calculatePrice(int quantity) {
        return price * quantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }
    }
}
