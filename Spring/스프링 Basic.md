## IOC , DI, 컨테이너

### 제어의 역전 IOC 
- 기존 프로그램은 클라이언트 구현객체가 스스로 필요한 서버 구현 객체를 생성했었다. 구현 객체가 제어흐름을 조종했다.
- AppConfig 설정을 통해 구현객체는 자신의 로직 실행만을 담당하게 한다. 프로그램의 제어흐름은 이제 AppConfg가 가져간다. 
- 프로그램에 대한 제어흐름 권한 모두 AppConfig 가 가지고 있다. OrderServiceImpl 생성 또한 Config에서 담당하며, OrderServiceImpl 이 아닌 다른 OrderService 인터페이스의 구현 객체를 생성하고 실행할 수도 있다.
- 프로그램의 제어 흐름을 직접 제어하는 것이 아닌 외부에서 관리하는 것을 제어의 역전(IoC)라고 한다.

라이브러리 Vs 프레임워크
 - 프레임워크가 내가 작성한 코드를 제어하고 대신, 실행한다면 프레임워크(JUnit)
 - 내가 작성한 코드가 직접 제어의 흐름을 담당하다면 라이브러리이다.

### 의존관계 주입 DI

- OrderServiceImpl 은 DiscountPolicy 인터페이스에 의존한다. 실제 어떤 구현체가 사용될지는 모름.'
- 의존관계는 "정적인 클래스 의존관계와, 실행 시점에 결정되는 동적인 객체(인스턴스)" 의존 관계 둘을 분리함.

"정적인 클래스 의존관계"

 : Diagram > show Diagram > Show Dependency (인텔리제이)
   
 : 클래스가 사용하는 Import 코드만 보고 의존 관계를 쉽게 파악가능. 
 
   애플리케이션을 실행하지 않아도 알 수 있음. 이러한 클래스 의존관계만으로는 실제 어떤 객체가 구현객체(impl)에 주입 될지 알 수 없다.
   
"동적인 객체 인스턴스 의존관계"
 
  : 애플리케이션 실행 시점에 실제 생성된 객체 인스턴스의 참조가 연결된 의존관계다.
  
 -  애플리케이션 실행시점(런타임)에 외부 실제 구현 객체를 생성함.
     클라이언트에 전달해서 클라이언트와 서버의 실제 의존관계가 연결되는 것을 의존관계 주입
 - 의존관계 주입을 사용하면,  정적인 클래스 의존관계를 벼경하지 않고 동적인 객체 인스턴스를 쉽게 벼경 가능하다. 
  
  
  ### IoC 컨테이너, DI 컨테이너
  
  - AppConfig 처럼 객체를 생성하고 관리, 의존관계를 생성하는 것.
  - ioc 컨테이너, DI 컨테이너라 한다.
  - 의존관계 주입에 초점을 맞추어 최근엔 주로 DI 컨테이너라 한다.
  - 또는 어샘블러, 오브젝트 팩토리 등으로 불리기도 함.
  
  ### 스프링 컨테이너
  
  - ApplicationContext 를 스프링 컨테이너 라 한다.
  - 기존에는 AppConfig 를 사용해서 직접 객체를 생성하고 DI를 했으나, 스프링 컨테이너를 토해서 사용함ㄴ다.
  - 스프링 컨테이너는 @Configuration 이 붙은 설정 정보로 사용함. 여기서 @Bean 이 적힌 메소드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다.
  - 스프링 컨테이너에 등록된 객체를 스프링 빈이라 한다.
  - applicationContext.getBean() 메서드를 통해서 Bean을 찾을 수 있다.
  - 스프링 컨테이너의 어떤 장점이 있을까??? -> 스프링 컨테이너가 관리해줌으로써, 지원 받는 기능이 많음.


# 스프링 컨테이너와 스프링 컨테이너 빈

## 스프링 컨테이너 생성
```java
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
```
- ApplicationContext 는 인터페이스이다.
- 스프링 컨테이너는 xml, 애노테이션 기반의 java 설정 클래스로 만들 수 있음.

1. 스프링 컨테이너 생성
2. 스프링 빈 등록 ( 스프링 컨테이너는 파라미터로 넘어온 설정  클래스 정보를 사용해서 스프링 빈을 등록한다.)
 - 빈 이름은 메서드 이름을 사용한다.
 - @Bean(name = "imsi") 임의로 부여할 수 있다.
 - **주의 : 빈이름은 항상 다른 이름을 부여**

3. 스프링 빈 의존관계 설정 - 준비
   -  스프링 빈을 먼저 생성
4. 스프링 빈 의존관계 설정 - 완료
   - 스프링 컨테이너는 설정 정보를 참조해 의존관계를 주입한다.
   - 단순히 자바 코드를 호출하는 것 같지만, 차이가 있으며 뒤의 싱글톤 컨테이너에서 설명 예정
 
  **참고**
  스프링은 빈을 생성하고, 의존관계를 주입하는 단계가 나누어져 있다.
  
  자바 코드로 스프링 빈을 등록하면 생성자를 호출하면서 의존관계 주입도 한번에 처리됨.
  
  여기서는 이해를 돕기 위해 개념적을 나눈 것임.
  
  ## 스프링 컨테이너에 등록된 모든 빈 조회
  
 ```java

import hello.core.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean(){
        String [] beanDefinitionNames = ac.getBeanDefinitionNames();
        for(String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);
            System.out.println("name ="+beanDefinitionName+" object = "+ bean);
        }

    }
    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    void findApplicationBean(){
        String [] beanDefinitionNames = ac.getBeanDefinitionNames();
        for(String beanDefinitionName : beanDefinitionNames){
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
            //ROLE_APPLICATION : 직접 등록한 애플리케이션 빈
            //ROLE_INFRASTRUCTURE : 스프링 내부에서 사용하는 빈
            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION){
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name ="+beanDefinitionName+" object = "+ bean);
            }
            }
        }
}
```


## 스프링 빈 조회 기본

```java
package hello.core.beanfind;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextBasicFindText{
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName(){
        MemberService memberService = ac.getBean("memberService", MemberService.class);

        // MemberServiceImpl 객체
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름없이 타입으로만 조회")
    void findBeanByType(){
        MemberService memberService = ac.getBean(MemberService.class);

        // MemberServiceImpl 객체
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2(){
        MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);

        // MemberServiceImpl 객체
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("빈 이름으로 조회가 안됨(없음)")
    void findBeanByNameNotFound(){
        //MemberService xxxx = ;

        assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("xxxx", MemberService.class));
    }
}


## 스프링 빈 조회 - 동일한 타입이 둘 

- 타입으로 조회시 같은 스프링 빈이 둘 이상이면 오류 발생한다(NoUniqueBeanDefinitionException). 이때는 빈 이름을 지정해야 함.
- ac.getBeanOfType() 을 사용하면 해당하는 타입의 모든 빈을 조회




```
