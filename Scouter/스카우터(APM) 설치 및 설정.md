# 개요, APM 이란?

※ APM (Application Performance Management)
    : Application의 성능을 관리하는 서비스이며, 주로 웹 서비스를 제공하는데 있어 
      안정적으로 운영할 수 있도록 지원하는 서비스

# 스카우터 설치

[스카우터 최신버전 다운로드](https://github.com/scouter-project/scouter/releases)

scouter-all-[version].tar.gz 설치 (스카우터 Collector와 Agent 를 포함한 파일, UNIX환경에서 .gz 가 없어서 tar로 재압축함..)
scouter.client.product-[os].zip (스카우터 클라이언트 - 각 OS 별 Viewer  Java 설치 必, 최신버전의 경우 openjdk 1.8의 경우 오류 발생 확인)

## 스카우터 서버 설치 및 기동
1. 서버를 기동할 적절한 위치에 scouter-all-[version].tar.gz의 압축해제
2. Scouter Server를 실행
3. netstat –an | grep 6100 명령어 실행하여 Scouter serve이 LISTEN 하고 있는 것을 확인

## 스카우터 클라이언트
1. scouter.client.product-[os].zip의 압축을 풀고 실행
2. 접속할 Scouter Server의 IP 또는 도메인을 입력
3. ID와 Password를 입력 

## Host Agent 실행
1. scouter 밑  ./host.sh 명령으로 실행 
2. ./stop.sh 명령으로 중지 가능
3. Host Agnet 가 정상적으로 실행되면, Scouter Client 에서 확인 가능
4. Scouter Client에서 Default Object Type을 맞춰주어야 정상적으로 조회 (Unix 없지만 Linux 로 가능)
![image](https://user-images.githubusercontent.com/99159721/157774939-8f4a5aed-20ab-4827-9a07-b2794cc04908.png)

## Java Agetn 실행
Java Agent는 Server 나 Host 처럼 단독 실행되는 것이 아니라 Java Program 이 실행될 때 Attach 하여
    모니터링을 수행하므로 명령행 옵션을 설정 해야함
    ☞ -javaagent:/[somewhere]/scouter.agent.jar

Java Agent Jar 경로 지정, 설정파일 경로 지정, 모니터링 대상(Object) 이름 지정을 아래와 같이 적용

[Jeus]
-javaagent:/[스카우터설정파일경로]/scouter.agent.jar -Dscouter.config=/[스카우터설정파일경로]/scouter.conf -Dobj_name=obj명


### scouter.conf 설정

### scouter java agent configruation sample
obj_name=tomcat
net_collector_ip=127.0.01 -> 실제 운영중인 스카우터 서버 IP
net_collector_udp_port=6100
net_collector_tcp_port=6100
# paper topology (java 1.8)
counter_interaction_enabled=true
# http header 정보를 profile 합니다. profile_http_header_key가 정의되지 않았으면 전체 header를 profile
profile_http_header_enabled=true
# profile할 http header의 이름들을 comma로 구분하여 기입
profile_http_header_keys=
# Form parameter 또는 query string을 profiling 하기 위해서는 아래 옵션을 사용
profile_http_parameter_enabled=true
profile_http_querystring_enabled=true
# Spring의 request mapping method의 parameter를 profiling
# profiling되는 값은 각 파라미터 오브젝트의 toString() method의 값
profile_spring_controller_method_parameter_enabled=true
# 옵션 중 "hook_"로 시작하는 옵션은 대상 object(tomcat등)를 재시작해야 적용
# method profiling은 public method에만 작동, 다른 접근 제한자에 대한 profiling을 허용하려면 다음 옵션을 사용
hook_method_access_public_enabled=true
hook_method_access_private_enabled=true
hook_method_access_protected_enabled=true
# Activating default Method hooking
hook_method_access_none_enabled=true
hook_method_exclude_patterns=
hook_method_ignore_classes=
hook_method_ignore_prefixes=get,set
hook_method_patterns=
# XLog 정보에는 ip 정보가 포함되는데 proxy 등을 경유하는 경우 사용자 IP를 http header에서 구해오도록 설정
trace_http_client_ip_header_key=
# error로 마킹할 Exception class pattern을 지정
# 상속받은 Exception class의 이름까지 검색
hook_exception_class_patterns=
# 위 패턴에서 제외할 패턴을 지정
hook_exception_exclude_class_patterns=
# Exception handler method의 pattern을 지정, 이 메소드로 전달되는 Exception class의 정보로 error 정보를 profiling
hook_exception_handler_method_patterns=
# 위 메소드에 전달되는 Exception 중 error 마킹에서 제외할 class 정보를 입력
hook_exception_handler_exclude_class_patterns=
# error 발생시 profile에 full stack trace를 남기거나 기본 제공되는 error 옵션을 변경하려면 아래 설정들을 사용
profile_fullstack_hooked_exception_enabled=false
xlog_error_on_sqlexception_enabled=true
xlog_error_on_apicall_exception_enabled=true
xlog_error_on_redis_exception_enabled=true
xlog_error_sql_time_max_ms=30000
xlog_error_jdbc_fetch_max=10000
##### [XLog Sampling] #####
# xlog는 유지하고 profile 정보만 샘플링
xlog_sampling_enabled=false
xlog_sampling_step1_ms=100
xlog_sampling_step1_rate_pct=3
xlog_sampling_step2_ms=200
xlog_sampling_step2_rate_pct=10
xlog_sampling_step3_ms=500
xlog_sampling_step3_rate_pct=30
xlog_sampling_over_rate_pct=100
##### [XLog Patterned Sampling] #####
xlog_patterned_sampling_enabled=false
xlog_patterned_sampling_service_patterns=
# xlog는 유지하고 profile 정보만 샘플링
xlog_patterned_sampling_only_profile=false
xlog_patterned_sampling_step1_ms=100
xlog_patterned_sampling_step1_rate_pct=3
xlog_patterned_sampling_step2_ms=1000
xlog_patterned_sampling_step2_rate_pct=10
xlog_patterned_sampling_step3_ms=3000
xlog_patterned_sampling_step3_rate_pct=30
xlog_patterned_sampling_over_rate_pct=100
# 보통 health check 요청등을 무시하기 위해 사용
xlog_discard_service_patterns=
# error 발생시에는 xlog에 기록한다.
xlog_discard_service_show_error=true
#Activating collect sub counters using JMX
jmx_counter_enabled=true

### 모니터링

모니터링 화면
![image](https://user-images.githubusercontent.com/99159721/157775513-d771b7de-a92d-41e4-8a66-3680067d1457.png)


Xlog_java 추가
![image](https://user-images.githubusercontent.com/99159721/157775675-28dba79e-1e1d-4d01-b922-fb9118eb9033.png)

※ XLog의 주요 메타 정보
service : service명
elapsed : 응답시간
endtime : 요청의 종료시간
ipaddr: 요청자의 ip
userAgent: 요청자의 브라우저 정보
sqlCallCount, sqlCallTime : 실행한 SQL 개수와 수행시간
apiCallCount, apiCallTime : 외부로 http call을 수행한 개수와 시간
cpu: 해당 요청을 처리하는 동안 사용한 cpu time
kbytes: 해당 요청을 처리하는 동안 사용한 heap memory
thread: 처리한 thread명
country, city : geoip 정보 (서버에 geoip_data_city_file 옵션 설정시)
queuing time : proxy 서버로 부터 서버로 부터 인입까지 걸린 시간(설정시)


