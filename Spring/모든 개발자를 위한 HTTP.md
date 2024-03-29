## 인터넷 통신

### IP(인터넷 프로토콜)
  : 지정한 IP 주소에 데이터 전달, 패킷이라는 통신단위 전달
  : IP 패킷정보라는 규칙 (출발지 IP, 목적지IP, 기타)으로 전달
 
 IP 프로토콜의 한계
  - 비연결성 ( 패킷을 받을 대상이 없거나 서비스 불능이여도 패킷전송)
  - 비신뢰성 ( 패킷 중간에서 사라지거나, 순서대로 안오면)
  - 프로그램 구분 ( 같은 IP 서버에서 통신하는 애플리케이션이 둘 이상인겨우)
  
 대상 서비스 불능, 패킷 전송
 대상서버가 패킷을 받을 수 있는 상태인지 모르고, 우선 전송
 패킷 소실(중간 서버에서 문제가 발생되어, 없어짐)
 패킷 전달 순서 문제발생
 (패킷 메시지가 큰 경우, 나눠서 보내게 됨 대략 1500Byte)
 -> 한계점을 TCP,UDP로 극복하고자 함
 
### TCP, UDP

인터넷 프로토콜의 4계층
1. 애플리케이션 계층
2. 전송계층(TCP,UDP)
3. 인터넷 계층
4. 네트워크 인터페이스 계층

TCP/IP 패킷 정보(출발지 Port, 목적지 Port, 전송제어,순서,검증)

TCP 특징
전송제어 프로토콜
연결지향 - TCP 3 wah Handshake(가상 연결)
데이터 전달보증
순서 보장
신뢰할 수 있는 프로토콜

#### 3 way handshake

1. SYN이라는 메시지를 서버에 전송
2. SYN+ACK 메시지를 클라이언트에 전송
3. 클라이언트에서 ACK 메시지 전송(데이터도 함께 전송)
* SYN 접속요청, ACK 요청수락

실제 연결이 아닌 개념적으로 연결됨.

#### 데이터 전달 보증
1. 데이터 전송
2. 데이터 잘 받았음(서버에서 응답)

#### 순서 보장
1. 패킷1,2,3, 순서로 전송
2. 서버에 패킷1,3,2 로 도착
3. 서버에서 패킷 2부터 다시 보내라는 요청을 줌.

-> Tcp 패킷에 전송정보, 전송 순서가 있어 확인 가능

#### UDP

사용자 데이터그램 프로토콜
기능이 거의 없음.
데이터 전달 및 순서가 보장되지 않지만, 단순하고 빠름.
IP와 같으나 Port와 체크섬 정도만 있음.
애플리케이션에서 추가 작업 필요.

### Port

한번에 둘 이상을 연결해야 하면?

Port - 같은 IP 내에서 프로세스를 구분
Ex. 아파트를 서버, 동호수를 Port 로 생각
TCP/IP 기본적으로 포트정보를 담음.
0~65535 할당가능
0~1023 잘알려진 포트
FTP : 20,21
TELNET : 23
HTTP : 80
HTTPS : 443

### DNS

IP 기억하기 어려움.
변견될 수 있음.

도메인 네임 시스템 
도메인명을 IP 번호로 변환

DNS 사용
DNS 서버에 등록
1. 도메인명을 도메인 서버를 전송
2. DNS 서버에서 IP 정보 응답

### 1일차 정리

복잡한 인터넷망에서 전송하기 위해서
IP 프로토콜이 있어야 하나 한계점이 있었음.
이를 해결하기 위해 TCP 프로토콜로 해결
UDP는 IP와 유사하나 필요한 경우 애플리케이션 프로그램에서 확장
Port 같은 IP에서 애플리케이션을 구분함.
DNS 도메인명의 등록을 바탕으로 IP 변경, 기억할 필요가 없음.

## URI 와 웹 브라우저 요청흐름

URL : 리소스의 위치
URN : 리소스의 이름

거의 url을 사용

URI : 리소스를 식별하는 통일된 방식 / 자원, URI로 식별할 수 있는 모든 것 / 다른항목과 구분

위치는 변할 수 있지만, 이름은 변하지 않음.

URN 이름만으로 실제 리소스 찾는 방법이 보편화 X
-> URL - URL로 설명

#### URL 분석
전체 문법

scheme : 주로 프로토콜을 사용 ex) http, https, ftp 등등
 
userinfo : 거의 사용 X

Host 정보

Port : 포트정보

Paht : 리소스 경로, 계층적 구조
       
       ex) /home/~ , /members

query : Key = value 형태 , ?로 시작함 & 를 통해 추가 가능

쿼리스트링, 쿼리 파라미터 등으로 불리며, 웹서버에 제공하는 파라미터 문자형태

fragment : html 내부 북마크

#### 웹브라우저 요청 흐름

DNS 조회를 통해 IP 를 찾음. Http, Https Port 생략

HTTP 메시지 전송

1. 웹브라우저가 HttP 메시지 생성

2. socket 라이브러리를 통해 전달
   A : TCP/IP 연결(IP,Port)
   B : 데이터 전달

3. TCP/IP 패킷 생성/전달 ( Http메시지 )

4. 서버에서 HTTP 응답 메시지를 만들어서 응답 패킷 생성

5. 웹브라우저에서 HTML 렌더링

#### HTTP

HTTP : HyperText 전송 프로토콜로 시작함.

현재는 모든 것을 전송할 수 있음.
IMG, 음성 영상, 파일 , JSON, 서버간에 데이터를 주고 받을 때도 대부분 HTTP

HTTP 역사

1.1 1997년 : 가장 많이 사용, 우리에게 가장 중요한 버전
이후 버전은 성능 개선

기반 프로토콜
HTTP/1.1 , 1.2 - TCP

HTTP/3 - UDP 
현재는 HTTP/1.1로 주로 사용 
 HTTP/2, HTTP3도 점점 증가세

크롬 브라우저 > F12 > 네트워크 > 프로토콜 (오른쪽)

#### HTTP 클라이언트 서버 구조

Request Response 구조

클라이언트는 서버에 요청을 보내고, 응답을 보내기
서버가 요청에 대한 결과를 만들어서 응답

서버와 클라이언트의 개념적으로 분리

비즈니스 로직, 데이터는 서버

클라이언트는 UI/UX

클라이언트/서버의 각 기술을 집중할 수 있음.(독립적)

#### HTTP 특징 - 무상태 프로토콜 (Stateless)

Stateful, Stateless 차이

Stateful - 서버가 클라이언트의 이전 상태를 보존함/유지함

Stateless - 고객이 요청시마다 필요한 데이터를 넘김

상태유지: 중간에 다른 점원으로 바뀌면 안된다. 
 (중간에 다른 점원으로 바뀔 때 상태 정보를 다른 점원에게 알려줘야 한다.)

무상태 : 중간에 다른 점원으로 바뀌어도 된다.
 - 갑자기 고객이 증가해도 점원을 대거 투입할 수 있다.
 - 갑자기 클라이언트 요청이 증가해도 서버를 대거 투입 가능
 - 무상태는 응답 서버를 쉽게 바꿀 수 있다. -> 무한한 서버 증설

StateFul : 같은 서버를 호출 해야하며, 장애 날 경우 / 클라이언트에서 처음부터 다시 호출 해야함.
StateLess : 아무 서버나 호출 가능하며, 중간에 장애나도 다른 서버로 응답

실무에서의 한계
 - 모든 것을 무상태로 설계할 수 있는 경우도, 없는 경우도 있음.
 - 상태 유지 ex)로그인
 - 무상태 ex) 로그인이 필요 없는 단순한 서비스 소개 화면
 - 상태 유지는 최소한만 사용
 - 전송 데이터를 더 많이 사용함.

#### HTTP 특징 - 비연결성

연결을 유지하는 모델
서버는 연결을 계속 유지하고 리소스를 사용해야 함.

연결을 유지하지 않는 모델
서버는 연결유지 X. 최소한의 자원 유지, 사용


비연결성
 - HTTP 기본이 연결을 유지 않음.
 - 일반적으로 초 단위 이하의 빠른 속도로 응답
 - Ex) 1시간 동안 수천명이 사용해도 실제 서버에서 동시 처리하는 요청은 수십개 이하로 매우작음
 - 서버 자원을 매우 효율적으로 사용

한계와 극복
 - TCP/IP 연결을 새로 맺어야 함. => 3WAY Handshake 시간이 추가됨.
 - 웹 브라우저 사이트를 요청하면 HTML 뿐만 아니라 JavaScript,CSS, 이미지 등의 자원에 대해서도 연결/종료
 - HTTP 지속연결로 문제 해결
 - HTTP/2,3에서는 더욱 최적화
 - 초기 연결, 종료가 낭비됨
 - 지속연결 시 요청시 HTML 뿐만 아닌 다른 리소스의 요청이 이루어지고 난 다음 종료가 이루어짐

스테이스리스를 기억하자

- 정말 같은 시간에 딱 맞추어 발생하는 대용량 트래픽
- EX) 선착순 이벤트, 명절 KTX 예약, 학과 수업등록 등의 수만명 동시 요청
- 최대한 Stateless 로 설계하자
- 이벤트 설계할 때 첫 페이지는 정적 페이지를 뿌림. 그 후 이벤트 참여하도록 해서 최대한 부하를 줄여보도록 함.


#### HTTP 특징 - HTTP 메시지

요청과 응답이 구조가 다름.

Start-line (시작라인) 

Header

empty line(공백, CRLF)

Message Body


시작라인은
크게 Request Line / status Line 

Request LIne = method SP(공백) request-target SP HTTP version CRLF(엔터)

HTTP 메서드 : GET,PUT,POST, DELETE

요청경로 : 절대경로 + ?쿼리

마지막은 HTTP 버전

응답메시지는 status-line 으로 시작함.
status-line = HTTP-version SP status-code SP reson-phrase CRLP

HTTP버전, HTTP 상태코드, 이유문구


HTTP헤더
- header-field = field-name ":" OWS filed-value OWS (띄어쓰기 허용)
- HTTP 전송에 필요한 모든 부가정보 Ex) 메시지 바디의 내용, 크기 , 압축, 인증정보, 클라이언트 브라우저 정보, 캐시관리 정보, 서버 정보 
- 표준 헤더가 너무 많음
- 필요 시 임의의 헤더 추가 가능

HTTP 메시지 마디
- 실제 전송할 데이터
- HTTP문서, 이지, 영상, JSOn 등으로 Byte로 표현할 수 있는 데이터.

단순함 / 확장 가능
HTTP는 단순하며, 메시지형태 또한 단순함.


### HTTP 메서드

#### EX) API URL 설계 회원
1. 회원 목록조회
2. 회원 조회
3. 회원등록
4. 회원 수정
5. 회원 삭제

설계 시 리소스 식별

리소스의 의미는 뭘까?
 - 회원을 등록하고 수정하고 조회하는 게 리소스가 아님.
 - 회원이라는 개념 자체가 리소스
 
리소스를 어떻게 식별하는 게 좋을까?
 - 회원을 등록/수정/조회 등은 배제
 - 회원이라는 리소스
 
 리소스 식별, URI 계층구조 활용했을 때
 어떻게 구분할까? (조회,등록,수정,삭제)
 - URI는 리소스만 식별
 - 리소스와 해당 리소스를 대상으로 하는 행위를 분리
 - 리소스 : 명사, 행위는 동사
 - 행위와 메서드는 어떻게 구분하는가? (HTTP 메서드)

#### HTTP 메서드 - GET , POST

주요 메서드
 - GET : 리소스 조회
 - POST : 요청 데이터 처리, 주로 등록에 사용
 - PUT : 리소스를 대체, 해당 리소스가 없으면 생성
 - PATCH : 리소스 부분 변경
 - DELETE : 리소스 삭제
 
기타 메서트
 - HEAD : GET 동일하지만 메시지 부분 제외 상태줄과 헤더만 반환
 - OPTIONS : 주로 CORS 사용함. 대상리소스에 대한 통신 옵션

##### GET

리소스 조회

서버에 전달하고 싶은 Query 를 통해서 전달

메시지 바디를 사용해서 전달할 수 있지만, 지원하지 않는 곳이 많아서 권장하지 않음.

1. 리소스 조회 - 메시지 전달
2. 서버 도착
3. 응답 데이터를 다시 클라이언트

#### POST

요청 데이터 처리

메시지 바디를 통해 서버로 요청 데이터 전달

서버는 요청데이터를 처리
 - 메시지 바디를 통해 들어온 데이터를 처리하는 모든 기능을 수행한다.

주로 전달된 데이터로 신규 리소스 등록, 프로세스 처리에 사용.

1. 리소스 등록1 - 메시지 전달
2. 서버에서 신규 리소스 식별자를 생성
3. 응답 데이터 201 - Location 

요청데이터를 어떻게 처리한다는 뜻일까 ??

Spec : POST 메서드는 대상 리소스가 리소스의 고유 한 의미 체계에 따라 요청에 포함된 표현을 처리하도록 요청한다.

Ex)
 
 HTML 양식에 입력된 필드와 같은 데이터 블록을 데이터 처리 프로세스 에 제공
 - HTML FORM 에 입력한 정보로 회원가입, 주문 등
 
 게시판, 뉴스 그룹, 메일링, 블로그 또는 유사한 기사 그룹에 메시지 게시

**정리** : 1. 새 리소스 등록 
       2. 요청데이터 처리(단순히 데이터 생성/변경을 넘어서 프로세스 처리해야 하는 경우), POST의 새로운 리소스가 생성되지 않을 수 있음.
         POST의 결과로 새로운 리소스가 생성되지 않을 수 있음.
       - EX) POST /orders/{orderId}/start-delivery (컨트롤 URI)
       3. 다른 메서드로 처리하기 애매한 경우
        - JSON으로 조회 데이터를 넘겨야 하는데, GET을 사용하기 어려운 경우
        - 애매하면 POST
        
        
### HTTP PUT, PATCH DELETE 

#### PUT
리소스를 대체
 - 리소스가 있으면 대체
 - 리소스가 없으면 생성
**중요! 클라이언트가 리소스를 식별'**
 - 클라이언트가 리소스 위치를 알고 URI 지정함 EX) /members/100
 - POST와의 차이점
 
 **주의** 리소스를 완전히 대체됨. 일부만 변경이 아님.
 
 #### PATCH
 
 리소스 부분 변경함.
 
 PATCH 가 지원이 안되는 경우 - POST 로 대체
 
 #### DELETE
 
 리소스 제거
 
 
 ### HTTP 메서드의 속성
 1. 안전
 2. 멱등
 3. 캐시 가능

#### 안전 SAFE

호출해도 리소스를 변경하지 않는다.
 - Q. 계속 호출해서 로그 쌓여서 장애가 발생할 경우?
 - A. 안전은 해당 리소스만 고려하는 것. 그런 부분 까지 고려하지 않음.

#### 멱등 Idempotnent

한번 호출하든 두 번 호출하든 100번 호출하든 결과가 동일함.

멱등메서드
 - GET : 한번 조회, 두 번  조회하든 결과가 동일
 - PUT : 결과를 대체함. 따라서 같은 요청을 해도 최종결과가 동일
 - DELETE : 삭제된 결과는 여러 번 해도 동일함.
 - * POST는 멱등이 아니다. Ex) 두 번 호출 시 다름 / 결제, 주문 등

활용
 - 자동 복구 메커니즘 ( 응답이 없을 때 동일한 요청을 여러 번 보냄)
 - 서버가 TIMEOUT 등으로 정상 응답을 못주었을 때, 클라이언트에서 같은 요청을 다시 해도 되는가?

Q. 재요청 중간에 다른 곳에서 리소스를 변경한다면?
 - 사용자 1 : GET 
 - 사용자 2 : PUT
 - 사용자 1 : GET -> 사용자 2의 영향으로 결과가 바뀜

A. 멱등은 외부 요인으로 중간에 리소스가 변경되는 것까지는 고려 안함.

### 캐시가능 Cacheable

응답결과 리소스를 캐시해서 사용해도 되는가?
- GET , HEAD, POST, PATCH 캐시 가능
- 실제로는 GET , HEAD 정도만 사용 중
- POST, PATCH 본문 내요까지 캐시 키로 고려해야하여, 구현이 쉽지 않음.

### HTTP 메서드 활용

1. 클라이언트에서 서버로 데이터 전송

데이터 전달 방식은 크게 2가지로 나눌 수 있음.

쿼리 파라미터를 통한 데이터 전송
- GET
- 정렬 필터(검색어)

메시지 바디를 통한 데이터 전송
 - POST, PUT, PATCH
 - 회원 가입, 상품 주문, 리소스 등록, 리소스 변경 등

4가지 상황 예시

1)정적 데이터 조회
  - 쿼리 파라미터 미사용
  - 이미지, 정적 텍스트 문서
  - 조회는 GET 으로 사용

2)동적 데이터 조회
 - 쿼리 파라미터 사용
 - 주로 검색, 게시판 목록에서 정렬필터
 - 조회 조건을 줄여주는 필터, 조회 결과를 정렬하는 정렬 조건에 주로 사용
 - 조회는 GET 사용
 - GET은 쿼리 파라미터(쿼리스트링) 사용해서 데이터 전달
3)HTML FORM을 통한 데이터 전송
  - POST 전송 - 저장
  - Content-Type : application/x-www-form-urlEncoded => HTTP 바디에다가 쿼리스트링을 넣음.
  - multipart/form-data 파일 전송 boundary로 나눠줌(웹브라우저에서)
4)HTTP API를 통한 데이터 전송
