### Catalina.out 로그

catalina.out에 중요 로그파일이 모두 적재(System.out.print, err 도 저장됨)

### 로그 초기화

cat /dev/null > catalina.out

*NULL COPY - NULL copy는 해당 파일을 열고 있는 프로세스가 죽기전에는 물리적인 디스크 사용률(du)을 줄일 수 없으며, 단지 로그 파일의 내용만을 줄일 수 있다. 
로그파일은 0 바이트로 보이나 물리적인 디스크 사용률은 프로세스를 내려야만 처리가 된다.
만일 아무 프로세스와도 연결되지 않은 단순 텍스트파일이 대상이라면 NULL copy 후 바로 물리적인 디스크 사용률이 처리된다.

### Tomcat 설정 파일(catalina.sh) 수정

touch "$CATALINA_OUT" 주석처리

"2>&1" \| /usr/sbin/rotatelogs "$CATALINA_OUT".%Y-%m-%d 86400 540 "&"
      #>> "$CATALINA_OUT" 2>&1 "&"

* 86400 : 일단위로 로테이션(초단위 설정)
* 540 : 영국 표준 시와 한국 시간 차이

### apache의 logrotate 을 이용해 로그를 정리하는 방법

/etc/crontab 파일에 corn.시리즈들이 등록되어있어야 주기적으로 logrotate가 실행

cd /etc/logrotate.d
vi ./tomcat
```VI
/svc/tomcat/tomcat7/*/logs/*.out {
 copytruncate
 maxsize 500M
 daily
 rotate 30
 compress
 missingok
 notifempty
 dateext
}
```
copytruncate : 기존 파일을 백업해서 다른 파일로 이동하고 기존 파일은 지워버리는 옵션

(이옵션을 넣지 않으면 현재 사용중인 로그를 다른이름으로 move하고 새로운 파일을 생성한다.)

(이옵션을 활용하면 postrotate를 통한 서비스 재시작 없이 무중단 로깅이 가능하다.)

maxsize : 파일 최대크기로 최대크기가 넘으면 로테이션하고 최대크기가 넘지 않으면 daily로 로테이션

daily : 로그파일을 날짜별로 변환

compress : 지나간 로그파일들을 gzip으로 압축

dateext : 순환된 로그파일의 날짜확장자

missingok : 로그파일이 없더라도 오류를 발생시키지 않음

rotate 30 : 로그 파일은 30개만큼 저장된 다음 제거되거나 메일로 보내짐

notifempty : 파일의 내용이 없으면 새로운 로그 파일을 생성 안함

maxage 30(숫자) : 30일 이산된 로그 파일 삭제


출처: https://goni9071.tistory.com/entry/리눅스-logrotate를-이용한-로그-정리 [고니의꿈]

