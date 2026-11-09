# Week 3 — 배포 및 API 테스트

- 배포 URL:  https://gdg-7th-be-mission-production-0be1.up.railway.app
- Swagger: https://gdg-7th-be-mission-production-0be1.up.railway.app/swagger-ui/index.html

## Postman 테스트 (배포 서버)
### 상품 등록
![img_5.png](_resources/img_5.png)
### 상품 조회
![img_6.png](_resources/img_6.png)
### 상품 구매
![img_7.png](_resources/img_7.png)
### 재고 추가
![img_8.png](_resources/img_8.png)
### 상품 삭제
![img_9.png](_resources/img_9.png)

## Railway MySQL 데이터 확인
| 단계 | 화면 |
| --- | --- |
| 등록 후 | ![img.png](_resources/img.png) |
| 구매 후 | ![img_1.png](_resources/img_1.png) |
| 재고 추가 후 | ![img_2.png](_resources/img_2.png) |
| 삭제 후 | ![img_3.png](_resources/img_3.png) |
| 재배포 후 | ![img_4.png](_resources/img_4.png) |

- 구매 후 재고가 차감되고(사과 30→28), 재고 추가 결과가 48로 나와 `@Transactional` 적용 후 변경 사항이 DB에 반영됨을 확인했다.
- 재배포 후에도 데이터가 그대로 남아, 서버와 분리된 외부 DB에 저장되고 있음을 확인했다.