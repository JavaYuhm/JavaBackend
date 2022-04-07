## Connection Leak이 있을 경우 세션이 상당 수 생성됨

오라클 DB : SELECT * from v$resource_limit where resource_name in ('processes', 'sessions', 'transactions')

sessions 값이 비정상적으로 높아짐.


Webadmin > Resource > DataSource 

### Connection Pool 설정
![image](https://user-images.githubusercontent.com/99159721/162127631-cde938bb-c981-4f38-81aa-cc486cec15f2.png)


Enable Wait 
 - false : 새로운 커넥션을 생성해서 제공, 해당 커넥션은 Pooling 되지 않은 일회용(disposable) 커넥션인 커넥션임.
 - true : 더이상 커넥션을 늘릴 수 없는 경우 Wait Time 설정값만큼 기다리고, 초과될 경우 Timeout Exception 

Application에서 코딩상의 실수로 PreparedStatement나 Statement를 사용하고, close()하지 않을 경우 
false 인 경우 이 disposable 상태의 연결이 Max값 이상으로 생기는 현상 발생함.

True로 하게 되면 WaitTimeoutException 이 시간이 초과될 경우 발생하게 된다.

### Severs 의 Basic info > Action on Resource Leak에서 Auto Close

![image](https://user-images.githubusercontent.com/99159721/162133445-eaac1e95-e1b2-491f-8079-264f109c209f.png)

해당 설정을 통해 서버 자체에서 Connection 을 close 해준다고 하지만,,
운영 중인 서버에선 which was not closed after being used. 로 실제로 .. DB 상에서도 세션을 유지하고 있음.

서버가 Down, 장애가 안나기 위해서 수동으로 destroy를 통해 connection들을 지웠다,, 

**결국 근본적으로 DB와 연결 후 close() 되지 않은 Application을 찾아 수정하는 것이 가장 깔끔함**

