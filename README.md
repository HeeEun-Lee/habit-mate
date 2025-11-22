# habit-mate

## 📌 개요

습관 형성 및 관리를 도와주는 서비스를 개발한다.

---

## 📘 프로젝트 소개

> HabitMate는 사용자가 자신의 습관을 등록, 조회, 완료, 삭제할 수 있는
>
>
> 간단하지만 확장 가능한 **Spring Boot 기반 백엔드 프로젝트**입니다.
>
> 테스트 주도 개발(TDD)을 통해 서비스 로직을 안전하게 검증하고,
>
> 유지보수성과 확장성을 동시에 확보하는 것을 목표로 합니다.
>

## 🧩 주요 기능

### 인증 기능

| 기능 | 설명 | HTTP Method | Endpoint|
| --- | --- |-------------| --- |
| 회원가입 | 새로운 사용자 등록 | `POST`      | `/auth/sign-up` |
|로그인 | JWT 토큰 발급 | `POST`      | `/auth/sign-in` |
|인증 확인 | 현재 로그인한 사용자 확인 | `GET`       | `/auth/test-auth` |

### 습관(Habit) 기능

| 기능 | 설명 | HTTP Method | Endpoint |
| --- | --- | --- | --- |
| 습관 등록 | 새로운 습관을 추가 | `POST` | `/habits` |
| 습관 조회 | 등록된 모든 습관 조회 | `GET` | `/habits` |
| 습관 완료 | 특정 습관 완료 처리 | `PUT` | `/habits/{id}/complete` |
| 습관 삭제 | 특정 습관 삭제 | `DELETE` | `/habits/{id}` |
| 완료율 확인 | 완료된 습관 비율 조회 | `GET` | `/habits/completion-rate` |

## ⚙️ 기술 스택

| 구분 | 기술 |
| --- | --- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5.7 |
| **Build Tool** | Gradle |
| **Testing** | JUnit 5, AssertJ |
| **IDE** | IntelliJ IDEA |
| **API Testing** | Postman |

---

## 🧪 테스트 구조

HabitMate는 **TDD(Test-Driven Development)** 원칙에 따라 설계되었습니다.

| 테스트 클래스 | 설명 |
| --- | --- |
| `HabitTest` | `Habit` 객체의 기본 동작 검증 |
| `HabitsTest` | 일급 컬렉션 `Habits`의 로직(완료율, 불변성 등) 테스트 |
| `HabitServiceTest` | 서비스 로직 및 예외 처리 검증 |
| `HabitMateApplicationTest` | 전체 애플리케이션 구동 통합 테스트 |

## 🔧 실행 방법
### 1) 저장소 클론
   `git clone https://github.com/서진희/habit-mate.git` 

   `cd habit-mate`

### 2) 설정 파일 준비

> 프로젝트에는 보안을 위해 application.yml이 포함되지 않습니다.
> 아래 템플릿을 참고해 src/main/resources/application.yml을 생성하세요:
>
> (프로젝트 제공) src/main/resources/application.yml.example 참고

`cp src/main/resources/application.yml.example src/main/resources/application.yml`

 >Windows 사용자는 application.yml.example을 복사하여
같은 디렉토리에 application.yml로 이름만 변경해 주세요.

### 3) 실행
   `./gradlew bootRun`

### 4) Swagger 문서 접속
   `http://localhost:8080/swagger-ui/index.html`

> 여기서 JWT 토큰을 입력하면 인증이 필요한 API도 테스트할 수 있습니다.

## 🚀 주요 설계 포인트

- 인증 흐름: 요청 → JWT Filter → UserDetailsService → Authentication 설정
- 예외 처리 공통화: CustomException + ErrorMessage Enum + GlobalExceptionHandler
- Habit 기능 인증 통합: Authentication → userId 기반 구조로 리팩토링
- API 문서화: Swagger(SecurityScheme + BearerAuth)
- 테스트 리팩토링: 인증 변경에 맞춰 전체 테스트 재작성

## 🎯 향후 계획

- 알림 스케줄링 기능(@Scheduled)
- Redis 기반 캐싱
- Refresh Token 도입
- Docker 기반 배포