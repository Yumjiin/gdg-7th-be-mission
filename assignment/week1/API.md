# 쇼핑몰 API 명세서 (Week 1)

## 개요

| 구분 | 기능 | Method | URL | 성공 코드 |
| --- | --- | --- | --- | --- |
| 소비자 | 상품 조회 | `GET` | `/products?name={상품명}` | 200 OK |
| 소비자 | 상품 구매 | `POST` | `/orders` | 200 OK |
| 관리자 | 상품 등록 | `POST` | `/admin/products` | 201 Created |
| 관리자 | 재고 추가 | `PATCH` | `/admin/products/{productId}/stock` | 200 OK |
| 관리자 | 상품 삭제 | `DELETE` | `/admin/products` | 200 OK |

- Base URL: `http://localhost:8080`
- 요청·응답 형식: `application/json`
- 관리자 API는 `/admin` 경로 아래에 두어 소비자 API와 역할을 구분한다.

---

## 소비자 (User)

### 1. 상품 조회

상품명으로 상품 정보를 조회한다. 동일한 상품명은 하나만 존재한다.

- **Method**: `GET`
- **URL**: `/products?name={상품명}`

**Request**

| 위치 | 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- | --- |
| Query | `name` | String | O | 조회할 상품명 |

```
GET /products?name=사과
```

**Response** `200 OK`

```json
{
  "id": 1,
  "name": "사과",
  "price": 1500,
  "stock": 30
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `id` | Long | 상품 ID (구매 시 사용) |
| `name` | String | 상품명 |
| `price` | Integer | 개당 가격 (원) |
| `stock` | Integer | 재고 수량 |

**Error**

| 상태 코드 | 상황 |
| --- | --- |
| 404 Not Found | 해당 이름의 상품이 없음 |

---

### 2. 상품 구매

하나 이상의 상품을 한 번에 구매한다. 구매한 수량만큼 재고가 차감된다.

- **Method**: `POST`
- **URL**: `/orders`

**Request Body**

```json
{
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 1 }
  ]
}
```

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `items` | Array | O | 구매할 상품 목록 (1개 이상) |
| `items[].productId` | Long | O | 상품 ID |
| `items[].quantity` | Integer | O | 구매 수량 (1 이상) |

**Response** `200 OK`

```json
{
  "totalPrice": 5000,
  "items": [
    { "name": "사과", "quantity": 2, "price": 3000 },
    { "name": "바나나", "quantity": 1, "price": 2000 }
  ]
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `totalPrice` | Integer | 총 구매 금액 (원) |
| `items[].name` | String | 상품명 |
| `items[].quantity` | Integer | 구매 수량 |
| `items[].price` | Integer | 해당 상품 소비 금액 (개당 가격 × 수량) |

**Error**

| 상태 코드 | 상황 |
| --- | --- |
| 400 Bad Request | 구매 목록이 비어 있거나 수량이 1 미만 |
| 404 Not Found | 존재하지 않는 상품 ID |
| 409 Conflict | 재고 부족 |

> 재고가 부족한 상품이 하나라도 있으면 전체 구매를 진행하지 않는다.

---

## 관리자 (Admin)

### 1. 상품 등록

새 상품을 등록한다. 상품 ID는 서버가 부여한다.

- **Method**: `POST`
- **URL**: `/admin/products`

**Request Body**

```json
{
  "name": "사과",
  "price": 1500,
  "stock": 30
}
```

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `name` | String | O | 상품명 (중복 불가) |
| `price` | Integer | O | 개당 가격 (원) |
| `stock` | Integer | O | 초기 재고 수량 |

**Response** `201 Created`

```json
{
  "id": 1,
  "name": "사과",
  "price": 1500,
  "stock": 30
}
```

**Error**

| 상태 코드 | 상황 |
| --- | --- |
| 409 Conflict | 같은 이름의 상품이 이미 존재 |

---

### 2. 재고 추가

기존 상품의 재고를 추가한다.

- **Method**: `PATCH`
- **URL**: `/admin/products/{productId}/stock`

**Request**

| 위치 | 이름 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- | --- |
| Path | `productId` | Long | O | 상품 ID |
| Body | `quantity` | Integer | O | 추가할 수량 (1 이상) |

```json
{
  "quantity": 20
}
```

**Response** `200 OK`

```json
{
  "name": "사과",
  "stock": 50
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `name` | String | 상품명 |
| `stock` | Integer | 추가 후 최종 재고 수량 |

**Error**

| 상태 코드 | 상황 |
| --- | --- |
| 400 Bad Request | 추가 수량이 1 미만 |
| 404 Not Found | 존재하지 않는 상품 ID |

---

### 3. 상품 삭제

상품을 삭제한다. 여러 개를 한 번에 삭제할 수 있다.

- **Method**: `DELETE`
- **URL**: `/admin/products`

**Request Body**

```json
{
  "productIds": [1, 3]
}
```

| 필드 | 타입 | 필수 | 설명 |
| --- | --- | --- | --- |
| `productIds` | Array\<Long\> | O | 삭제할 상품 ID 목록 |

**Response** `200 OK`

```json
{
  "products": [
    { "name": "바나나", "stock": 10 }
  ]
}
```

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| `products[].name` | String | 남아 있는 상품명 |
| `products[].stock` | Integer | 재고 수량 |

**Error**

| 상태 코드 | 상황 |
| --- | --- |
| 404 Not Found | 존재하지 않는 상품 ID가 포함됨 |

> DELETE 요청에 Body를 사용하는 것은 HTTP 표준(RFC 9110)에서 권장하지 않지만, 여러 ID를 전달하기 위해 이 명세에서는 Body를 사용한다.

---

## 비고

- 1주차 구현에서는 예외를 `RuntimeException`으로 처리하므로, 실제 오류 응답은 위 표와 달리 `500 Internal Server Error`로 반환된다. 위 상태 코드는 이후 예외 처리를 추가할 때의 목표다.
- 금액 단위는 원(KRW)이며 정수로 표현한다.