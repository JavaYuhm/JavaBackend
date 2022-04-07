## Connection Leak이 있을 경우 세션이 상당 수 생성됨

오라클 DB : SELECT * from v$resource_limit where resource_name in ('processes', 'sessions', 'transactions')

sessions 값이 비정상적으로 높아짐.


![image](https://user-images.githubusercontent.com/99159721/162127631-cde938bb-c981-4f38-81aa-cc486cec15f2.png)
