package gdg.hongik.mission;

import java.util.ArrayList;
import java.util.List;

// DB 대신 메모리에 상품을 보관하는 임시 저장소 (1주차 전용)
public class ProductStore {
    public static List<Product> products = new ArrayList<>();
    public static Long sequence = 1L;   // 다음에 부여할 상품 ID
}