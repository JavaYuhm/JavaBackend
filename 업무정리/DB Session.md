### Oralce DB 를 사용하고 있는 웹 시스템에서 WAS DB Connection Pool 의 Session이 계속 남아있는 현상

Jeus 설정의 Auto Close 및 Stmt Query Timeout을 설정했으나, 해결되지 않음.

DB내에서 해당 부분을 확인했을 때, InActive 상태로 남아있는 Session 들을 확인 가능함.

```SQL
select username,sid,serial#,status from v$session where status ='INACTIVE' and username ='DB사용자';
```

JEUS(WAS)의 설정상 Connection Max 값이 30 이므로, 최대 연결될 Session보다 더 연결이 많이 된 걸 확인함.

오라클에서 돌고 있는 쿼리 시간 및 쿼리 확인
``` SQL
  SELECT TO_CHAR (SID) sid, serial# serialNumber,
   SUBSTR (TO_CHAR (last_call_et), 1, 6) executeSeconds, userName, machine,
   b.sql_text sqlText
  FROM v$session a, v$sqltext b
  WHERE username NOT IN ('SYSTEM', 'SYS')
   AND a.TYPE != 'BACKGROUND'
   AND a.status = 'ACTIVE'
   AND a.sql_address = b.address(+)
   AND a.sql_hash_value = b.hash_value(+)
  ORDER BY a.last_call_et DESC,
   a.SID,
   a.serial#,
   b.address,
   b.hash_value,
   b.piece
```
출처: https://5dol.tistory.com/204 [5dol Story]

위의 쿼리를 통해 오랜 시간동안 남아있는 쿼리문 확인 
-> 해당 쿼리를 찍어내는 Application 에서 connection을 close하지 않는 걸 확인하고 해당 close 하는 로직 추가

레거시 시스템이고 해당 Java Source가 Class로 컴파일되어 있어서,, 로직 찾기가 힘들지만 디컴파일 후 해당 쿼리 사용하는 부분 수정함..
