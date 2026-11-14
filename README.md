# 7th Backend Mission Course 🛒

GDG on Campus Hongik Univ. 프로젝트 트랙 7기 백엔드 미션코스 저장소입니다.  
스프링 부트를 처음 접하는 개발자도 4주 동안 단계별로 학습하며, 하나의 쇼핑몰 백엔드 서버를 완성하고 프론트엔드 연동 및 배포까지 경험할 수 있도록 구성되어 있습니다.

---

## 🌿 1. 브랜치 안내 (Branch Guide)

본 레포지토리는 참가자분들의 원활한 학습과 미션 진행을 위해 브랜치별로 코드가 구성되어 있습니다.

- **`main`**: **초기 세팅 상태 (Base Code)**  
  미션을 시작하기 위한 기본 뼈대 코드(Spring Boot 초기 설정)가 위치한 브랜치입니다. 과제를 시작할 때 이 브랜치를 기준으로 작업합니다.
- **`weekN`**: **N주차 미션 예시 코드**  
  각 주차별 미션 마감 시점에 참고용 예시 코드가 해당 브랜치(`week1`, `week2` 등)에 순차적으로 업데이트됩니다.

> [!TIP]
> 주차별 예시 코드는 학습 도중 막히거나 패키지 구조/설계 방향이 막막할 때 참고용으로 확인하실 수 있습니다. (100% 정답 코드가 아니므로 본인의 설계에 맞춰 자유롭게 발전시켜 주세요!)

---

## 📌 2. 개요 및 학습 목표 (Overview & Goals)

### 🎯 목표
- **점진적 계층 분리**: `Controller` 단일 계층부터 시작하여 `Entity`, `Repository`, `Service`, `DTO` 계층으로 점진적으로 책임을 분리하며 스프링의 계층형 아키텍처(Layered Architecture)를 체득합니다.
- **스프링 핵심 메커니즘 이해**: 영속성 컨텍스트, Spring Bean, IoC/DI, `@Transactional`, 전역 예외 처리 등 스프링 프레임워크의 핵심 동작 원리를 학습합니다.
- **실전 협업 역량 강화**: 명확한 API 명세서 작성, Swagger 문서화, Postman 검증, Git Fork-PR 협업 흐름을 경험하고 프론트엔드와의 합동 API 연동 세션을 준비합니다.

### 🛠 기술 스택 및 개발 환경
- **Language**: Java 17
- **Framework**: Spring Boot 3.x / 4.x
- **ORM / Database**: Spring Data JPA, H2 Database (In-Memory)
- **Docs & Test**: Springdoc OpenAPI (Swagger-UI), Postman
- **Build Tool**: Gradle

### 📁 디렉토리 구조
```plain text
gdg-7th-be-mission/
├── assignment/
│   ├── week1/
│   │   ├── API.md
│   │   ├── SWAGGER.md
│   │   └── POSTMAN.md
│   ├── week2/
│   │   └── week2.md
│   ├── week3/
│   │   └── week3.md
│   └── week4/
│       └── week4.md
├── src/
│   ├── main/
│   │   ├── java/gdg/hongik/mission/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   └── dto/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── build.gradle
└── README.md
```

---

## 📚 3. 주차별 학습 내용 (Curriculum)

| 주차 | 주제 | 핵심 학습 내용 | 과제 산출물 |
|:---:|:---|:---|:---|
| **Week 1** | **HTTP, REST API & Controller 계층** | - 클라이언트-서버 모델, HTTP 통신 및 RESTful API 설계 원칙<br>- 쇼핑몰 기능(조회/구매/등록/재고수정/삭제) 명세 작성<br>- `Controller` 구현 및 인메모리 저장소(`ProductStore`) 활용<br>- Swagger 문서화 및 Postman API 테스트 실습 | `/assignment/week1/`<br>- `API.md`<br>- `SWAGGER.md`<br>- `POSTMAN.md` |
| **Week 2** | **Entity, Repository & Service 분리** | - `Product` 객체를 JPA `@Entity`로 전환 및 테이블 매핑<br>- Spring Data JPA `Repository` 인터페이스 및 영속성 컨텍스트 학습<br>- 비즈니스 로직 전담 `Service` 계층 분리 (관심사의 분리)<br>- Spring Bean의 개념, IoC/DI 및 싱글톤 컨테이너 이해<br>- 객체지향 4원칙 학습 및 H2 DB 기반 동작 검증 | `/assignment/week2/`<br>- `week2.md`<br>- Entity/Repo/Service 코드 |
| **Week 3** | **DTO 도입 & 트랜잭션 관리** | - Entity 직접 노출 방지 및 DTO 도입 필요성 학습<br>- 요청/응답 DTO 설계 및 규격화된 API 응답 포맷 일치화<br>- `@Transactional` 원리(더티 체킹, Flush, Commit/Rollback) 학습<br>- 프론트엔드 합동 API 연결 세션 대비<br>- *(선택)* Java Record 활용, AOP Proxy 동작 원리 학습 | `/assignment/week3/`<br>- `week3.md`<br>- DTO 적용 및 Service 트랜잭션 적용 코드 |
| **Week 4** | **유효성 검사, 전역 예외 처리 & 리팩토링** | - Bean Validation(`@Valid`, `@NotNull`, `@NotBlank` 등) 입력값 검증<br>- `@ControllerAdvice`, `@ExceptionHandler` 기반 전역 예외 처리 체계 구축<br>- SOLID 원칙 및 클린 코드 철학에 기반한 코드 리팩토링 (3가지 이상)<br>- *(선택)* 체크/언체크 예외 설계, JavaDoc 문서화 주석 작성 | `/assignment/week4/`<br>- `week4.md`<br>- 리팩토링 및 예외 처리 코드 |

---

## 🚀 4. 과제 제출 방법 (Submission Guide)

과제는 **코드 제출(GitHub Pull Request)**과 **WIL 작성(개인 기술 블로그)** 두 가지로 진행됩니다.

### Step 1. 레포지토리 Fork & Clone
1. 미션 원격 저장소 우측 상단의 **`Fork`** 버튼을 클릭하여 본인 GitHub 계정으로 저장소를 복제합니다.
2. 복제된 본인의 원격 저장소를 로컬 컴퓨터에 Clone합니다.
   ```bash
   git clone https://github.com/<본인-깃허브-아이디>/gdg-7th-be-mission.git
   cd gdg-7th-be-mission
   ```

### Step 2. 미션 수행 및 Commit & Push
1. 주차별 요구사항에 맞춰 코드를 구현하고, `assignment/weekN/` 디렉토리에 요구되는 문서/스크린샷을 작성합니다.
2. 작업이 완료되면 본인의 원격 저장소에 커밋 및 푸시합니다.
   ```bash
   git add .
   git commit -m "feat: N주차 미션 구현"
   git push origin main
   ```

### Step 3. Pull Request(PR) 생성
1. 본인의 GitHub 저장소 페이지 상단의 **`Contribute`** ➔ **`Open pull request`**를 클릭합니다.
2. PR 제목 컨벤션을 준수하여 제목을 작성합니다.
   - **PR 제목 형식**: `[이름] N주차 미션 제출합니다.`
   - *예시*: `[홍길동] 1주차 미션 제출합니다.`
3. **`Create pull request`** 버튼을 눌러 원본 저장소의 `main` 브랜치로 제출을 완료합니다.

### Step 4. WIL(Weekly I Learned) 블로그 작성 & 노션 제출
- **블로그 작성**: 한 주 동안 배운 핵심 개념, 설계 고민, 마주친 에러와 해결 과정을 **개인 블로그(Velog, Tistory 등)**에 작성합니다.
  - ⚠️ *WIL은 깃허브 마크다운 파일로 올리지 마시고, 개인 블로그에 작성해 주세요!*
- **노션 체크**:
  - 작성한 WIL 블로그 링크를 **미션코스 노션 페이지 제출란**에 등록합니다.
  - 노션의 제출 체크박스에 체크 표시를 완료합니다.
