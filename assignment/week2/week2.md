# Week 2 — Postman 동작 확인

## 상품 등록
![img_1.png](_resources/img_1.png)

## 상품 조회
![img_2.png](_resources/img_2.png)

## 상품 삭제
![img_3.png](_resources/img_3.png)

## H2 DB 확인
![img.png](_resources/img.png)
- 사과(id=1)는 삭제 요청 후 DB에서 사라졌다.
- 바나나는 구매로 1개를 샀지만 STOCK이 10 그대로다. 트랜잭션이 없어서 재고 변경(더티 체킹)이 DB에 반영되지 않았다. (3주차에서 다룰 내용)