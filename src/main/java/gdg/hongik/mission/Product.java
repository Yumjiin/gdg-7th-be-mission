package gdg.hongik.mission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor   // @RequestBody로 JSON -> 객체 변환 시 Jackson이 기본 생성자를 사용
@AllArgsConstructor
public class Product {

    private Long id;        // 상품 ID (서버가 부여)
    private String name;    // 상품명 (중복 불가)
    private Integer price;  // 개당 가격
    private Integer stock;  // 재고 수량
}