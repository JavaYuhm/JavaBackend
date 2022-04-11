### EDA 구성요소

Event : 

Producer :

Consumer :

Event Bus :

### Evnet Driven 제약사항

이벤트는 발생한 사건에 대해 약속된 형식 사용
불변 : 과거의 메시지를 변경할 수 없음
발생한 사건에 대한 결과 상태 전달
생성자(Producer)는 이벤트 처리 상태에 관여하지 않음.
소비자(Consumer)는 이벤트 생성자에 관심 갖지 않음

### 설계 시 고려사항

Loosely coupling
Removing dependencies
Nonblocking trnasaction
Asynchronized callback
Fallback / Retry
Event logging

### 
Single Producer - Single Consumer
 
Single Producer - Multi Consumer
- 이벤트 처리 비용이 이벤트 생성 비용보다 높은 경우 
Dead letter Queue
- 설정한 Retry limitation 동안 이벤트가 처리되지 않을 경우, Main 
message queue 에서 제거하고 사후 분석을 DLQ로 이동
Time to Live
처리 되지 않은 이벤트의 생명 주기를 제한하여 만료되면 이벤트 폐기

Asynchronus request-response 
- Polling
Event splitting
- 한 Event에 대해 여러 로직의 처리가 필요할 경우

### Event Driven Architecture 특장점
Service 간 호출이 많은 Microservice Architecture에 적합
Infra Structure의 유연한 사용
Polyglot
가용성, 응답성 Up -> 효율성, 성능 up
Falut Isolation
 - 하나의 Producer 또는 Consumer에 의해 서버 전체가 죽지 않음.

단점 : 
설계 복잡도 증가
Message Broker(브로커가 죽으면 동작 X) 의존성 증가
코드 가독성 하락
디버깅 난이도 상승
Log 를 통한 system flow 가독성 하락.

### Event Driven Programming

Consumer <> Producer 는 Broker 통해 소통
Message 생성 후 소비 전까지의 Queue 에 저장
Consumer <> Producer 의 분리로 인한 이점
 - N개의 Consumer, Producer 동시 처리
 - 실패한 작업 재시도 및 logging 로직 단순화
 - Consumer Scale out으로 컴퓨팅 리소스 확장
