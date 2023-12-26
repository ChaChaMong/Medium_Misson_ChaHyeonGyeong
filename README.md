
미션2: [미디엄 미션 상세 기능 요구사항](https://www.scode.gg/p/13293)

## 필수미션 1 : Member 클래스에 private boolean isPaid 필드를 추가
 - [ ] 해당 필드가 true 인 사람이 로그인할 때, ROLE_PAID 권한도 가지도록(스프링 시큐리티)  
해당 필드가 true 이면 유료 멤버십 회원 입니다.

## 필수미션 2 : Post 클래스에 private boolean isPaid 필드를 추가
 - [ ] 해당 필드가 true 인 글은 유료회원이 아닌사람에게는 상세보기(GET /post/1)에서 본문(content)이 나올 자리에  
"이 글은 유료멤버십전용 입니다." 라는 문구가 나온다.

## 필수미션 3 : NotProd 에서 유료멤버십 회원(샘플 데이터)과 유료글(샘플 데이터)을 각각 100개 이상 생성

## 선택미션 1 : 검색 필터링, 정렬

## 선택미션 2 : 글 본문에 마크다운 에디터 적용

## 선택미션 3 : 토스트 UI 에디터에 이미지 파일 업로드 기능 적용

## 선택미션 4 : 카카오 로그인

## 선택미션 5 : 위 모든 기능의 뷰를 타임리프 뿐 아니라 별도의 도메인에서 스벨트킷으로도 구현
- [x] /api/v1 로 시작하는 URL에서 API 구현
- [x] 루트 폴더에 frontapp 폴더 생성, 그 안에 스벨트킷 프로젝트 생성

## 선택미션 6 : 배포, 도메인 연결, 젠킨스 CI/CD

## 선택미션 7 : 배포, 도메인 연결, 쿠버네티스, 깃허브액션, 무중단 CI/CD

## 선택미션 8 : 정산기능구현

------

미션1: [미디엄 미션 상세 기능 요구사항](https://www.scode.gg/p/13201)

## 필수미션 1 : 회원기능
- [x] 가입
- [x] 로그인
- [x] 로그아웃

## 필수미션 2 : 글 CRUD
- [x] 홈
- [x] 글 목록 조회
- [x] 내 글 목록 조회
- [x] 글 상세내용 조회
- [x] 글 작성
- [x] 글 수정
- [x] 글 삭제
- [x] 특정 회원의 글 모아보기

## 선택미션 1 : 조회수
- [ ] 조회수 증가

## 선택미션 2 : 추천
- [ ] 추천

## 선택미션 3 : 댓글
- [ ] 댓글 목록
- [ ] 댓글 작성
- [ ] 댓글 수정
- [ ] 댓글 삭제

## 선택미션 4 : 검색 필터링, 정렬

## 선택미션 5 : 글 본문에 마크다운 에디터 적용

## 선택미션 6 : 토스트 UI 에디터에 이미지 파일 업로드 기능 적용

## 선택미션 7 : 카카오 로그인

## 선택미션 8 : 위 모든 기능의 뷰를 타임리프 뿐 아니라 별도의 도메인에서 스벨트킷으로도 구현
- [ ] /api/v1 로 시작하는 URL에서 API 구현
- [ ] 루트 폴더에 frontapp 폴더 생성, 그 안에 스벨트킷 프로젝트 생성

## 선택미션 9 : 배포, 도메인 연결, 젠킨스 CI/CD

## 선택미션 10 : 배포, 도메인 연결, 쿠버네티스, 깃허브액션, 무중단 CI/CD
