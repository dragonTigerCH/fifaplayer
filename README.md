# fifaplayer
피파온라인4 선수 평가 게시판

피파온라인4이라는 게임의 선수에 대한 평가를 작성하고 정보를 공유하는 게시판 

### 홈
선수 평점순으로 메인 사진 정렬 
![메인](https://user-images.githubusercontent.com/97586086/172292298-a194ff7f-0df3-48ee-8929-2bba46e52295.png)

### 1. 로그인
구글/페이스북/네이버 등으로 로그인 가능 (OAuth2.0)
![로그인](https://user-images.githubusercontent.com/97586086/172169083-e828ed5a-5676-4a78-89f0-63a38cbc6a23.png)

### 2. 비밀번호 찾기
SMTP를 사용하여 비밀번호 찾기 
![비밀번호 찾기](https://user-images.githubusercontent.com/97586086/172170456-459f485f-d1f3-4ef6-b6f2-97ca0fa8ff7b.png)
![인증번호 입력](https://user-images.githubusercontent.com/97586086/172170441-6eeda4a0-6bc1-4296-83b5-83b2789ff843.png)

### 3. 회원가입
유효성 검증(Validation)을 통한 회원가입(Spring Security)
![회원가입](https://user-images.githubusercontent.com/97586086/172179684-7136e5aa-c181-4820-bf78-d9eeec49f1d1.png)

### 4. 내정보 수정 및 내가 쓴 게시글,댓글
회원 탈퇴 및 비밀번호,닉네임 변경 작성한 댓글,게시글,선수 댓글 등을 볼수있는 마이페이지
![내정보 수정 및 내가 쓴 댓글,게시글](https://user-images.githubusercontent.com/97586086/172291851-b32ccfe4-3743-4e90-98a2-41e10c50e2fb.png)

### 5. 관리자만 등록 할수있는 선수 데이터들
관리자가 등록한 선수,시즌으로 종합적인 데이터를 만들어서 다른시즌 같은 선수로 만들수있게 설계 - 기본적인 유효성 검증(Validation)
![선수 시즌 데이터 등록](https://user-images.githubusercontent.com/97586086/172293757-ec4e7ef5-6a12-437e-b3c2-2e5714ba0ede.png)

### 6. 선수 실시간 평가 및 검색
선수 검색 및 실시간 평가
![선수 평가](https://user-images.githubusercontent.com/97586086/172293525-f838e6d0-7a5d-4b64-a24b-68dda29ebdb0.png)

### 7. 선수 능력치 비교 및 정렬
선수 능력치 별로 정렬 가능
![선수 비교 정렬](https://user-images.githubusercontent.com/97586086/172294144-ee06d224-862a-41f7-a2d7-b244ab890efa.png)

### 8. 선수 디테일 
평점 및 댓글, 대댓글(무한댓글)작성
![선수 디테일 평점 및 댓글 대댓글들](https://user-images.githubusercontent.com/97586086/172295134-201a204a-8e35-45c7-b5ae-28b19fe305ad.png)

### 9. 게시글 CRUD 
기본적인 게시글 CRUD 및 페이징처리
![게시글 CRUD 및 댓글](https://user-images.githubusercontent.com/97586086/172296477-46543d0c-7351-4477-ba8f-442e52f533b2.png)

### 프로젝트 구조

Spring Boot로 개발하였으며,
사용한 기술스택은 다음과 같습니다. 
• Spring Boot   
• Spring Security (Security)   
• H2 (RDB)  
• JPA & QueryDSL (ORM)   
• OAuth2.0 (Login)   
• JUnit (Test)    
• thymeleaf (template)    

config : security, oauth, querydsl 관련 기능들을 관리한다.    
exception : custom exception message를 관리한다.  
embeddable : jpa Embeddable 관리    
converter : boolean db컬럼 관리    
web    
controller : 사용자 요청 관리한다.    
form : request form를 관리한다.    
service : 정의한 business logic 호출 순서를 관리한다.    
repository : domain + JPA/QueryDSL를 관리한다.    

### JPA & QueryDSL (ORM)
객체 중심 domain 설계 및 반복적인 CRUD 작업을 대체해 비즈니스 로직에 집중한다. • JPA : 반복적인 CRUD 작업을 대체해 간단히 DB에서 데이터를 조회한다.

• QueryDSL : JPA로 해결할 수 없는 SQL은 QueryDSL로 작성한다.

구조는 다음과 같습니다.    
Board (Domain Class)      
BoardRepository (JPA Interface)      
BoardRepostioryCustom (QueryDSL Interface)    
BoardRepositoryCustomImpl (QueryDSL Implements Class)    
Datan (Domain Class)    
DatanRepository (JPA Interface)   
DatanRepostioryCustom (QueryDSL Interface)    
DatanRepositoryCustomImpl (QueryDSL Implements Class)   
Datan_Comments (Domain Class)    
Datan_CommentsRepository (JPA Interface)   
Datan_CommentsRepostioryCustom (QueryDSL Interface)   
Datan_CommentsRepositoryCustomImpl (QueryDSL Implements Class)    

### Spring Security (Security)

Security 설정을 추가해 인가된 사용자만 특정 URL에 접근할 수 있도록 제한한다. 
Anonymous 가 접근할 수 있어야 하는 API는 permitAll()을 선언했습니다.
또한 ROLE_USER, ROLE_ADMIN 권한 별 URL 제한했습니다.

### SMTP (Google)
Gmail의 SMTP 서버 설정과   
구글 앱 비밀번호를 통하여 회원가입, 비밀번호 찾기에 대한 인증번호를 메일에 담아 보냈습니다.

### OAuth2.0 (Login)

구글/페이스북/네이버 oauth provider를 사용해 불필요한 회원가입 프로세스를 제거한다. 

### JUnit (Test)

정의한 business logic에 대한 테스트 코드를 작성했습니다. 

![Junit](https://user-images.githubusercontent.com/97586086/172299660-948e1129-55c8-45f3-b195-f72115f0b2a9.png)

