package com.spring.aop;

import java.util.Arrays;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/* 참고: logginadvice securityadvice testadvice transactionadvice과 같이 이름을 지음.


# AOP( Aspect-Oriented Programming ) 관점 지향 프로그래밍 *면접!

	- 프로젝트 개발 과정에서 핵심 기능 외에 추가적이고, 다양한 부가(공통) 기능이 필요하다. (로깅,보안,트랜젝션,테스트)
	
	- 이 부가(공통)기능들은 프로젝트에 굉장히 중요한 역할을 하며 이 부가(공통)기능이 코드마다 반복적(중복)으로 나타나는 부분이 존재한다. (*쉽게 말해 '중복제거' 즉 필수는 아니라는 것)
	
	- 코드에서 비즈니스 핵심 로직과 부가기능을 분리하여 부가 로직을 따로 관리(모듈화)한다.
	                    (ex 핵심로직: MaindController 부가기능:Advice.jsp)
	                    
	- 종단(비즈니스 로직) 기능 , 횡단(관심 , Aspect) 기능 
	
	- 부가 기능이 비즈니스 로직(핵심 기능)을 담은 클래스의 코드에 전혀 영향을 주지 않으면서 부가기능의 구현을 용이하게 할 수 있는 구조를 제공한다.
	
	- AOP는 OOP를 대체하는 새로운 개념이 아니라 OOP를 돕는 보조적 기술 중에 하나 이다.
	
	- *중요* 스프링 DI  : 의존성(new)주입  , 스프링 AOP  : 로직(code) 주입
	
	- 용어 정리 

	1) Aspect	   : 관점
	2) Advice	   : 핵심기능에 부여되는 부가기능 ( 위치 메서드에 적용될 부가 기능 )
	
		2-1) Around (Advice)		 : 대상 객체의 메서드 실행 전,후 및 예외 발생 모두 실행한다.
		2-2) Before (Advice)		 : 대상 객체의 메서드 메서드 호출전에 수행한다.
		2-3) After (Advice)			 : 대상 객체의 메서드 실행도중 예외 발생 여부와 상관없이 메서드 실행 후 실행한다.
		2-4) AfterReturning (Advice) : 대상 객체의 메서드가 실행 도중 예외없이 실행 성공한 경우에 실행한다.
		2-5) AfterThrowing (Advice)  : 대상 객체의 메서드가 실행 도중 예외가 발생한 경우에 실행한다.
	
	3) Pointcut   : Aspect 적용 위치 지정자      ( Advice를 어디에 적용할지를 결정  )
	4) Advisor    : Advice + Pointcut
	5) Joinpoint  : Aspect가 적용한 지점


	[ 구현 실습 예시 ]
	
	1) pom.xml 파일에 AOP 관련 dependency 추가
	2) AOP설정 xml 파일에 aop autoproxy 지시
	3) target메소드에 추가 할 Aspect 생성
	4) 구현

*/

@Component
@Aspect // AOP에서 Aspect로 사용을 선언하는 어노테이션
public class Advice {
	
	/*
	 *  # execution 명시자 
	 *  
	 *  - execution(수식어패턴 리턴타입패턴 *클래스이름패턴?메서드이름패턴(파라미터패턴)) 
	 *  - 각 패턴은 *을 이용하여 모든값을 표현할 수 있다.
	 *  
	 *  
	 *  [패키지]
	 *  com.spring.aop	  > com.spring.aop패키지를 타겟
	 *  com.spring.aop..  > com.spring.aop로 시작하는 하위의 모든 패키지를 타겟
	 *  
	 *  
	 *  [리턴타입]
	 *  *		> 모든 리턴 타입 타겟 (주로 사용함)
	 *  void	> 리턴 타입이 void인 메서드만 타겟
	 *  !void	> 리턴 타입이 void가 아닌 메서드만 타겟 
	 *  
	 *  
	 *  [매개 변수 지정]
	 *  (..)		>  0개 이상의 모든 파라미터 타겟
	 *  (*)			> 1개의 파라미터만 타겟
	 *  (*,*)		> 2개의 파라미터만 타겟
	 *  (String,*)	> 2개의 파라미터중 첫번째 파라미터가 String타입만 타겟
	 *  
	 *  
	 *  [샘플 예시]
	 *  
	 *  execution(public void set*(..)) 					>> 리턴 타입이 void이고 메서드 이름이 set으로 시작하고 파라미터가 0개 이상인 메서드 타겟
	 *  execution(* abc.*.*()) 								>> abc패키지에 속한 파라미터가 없는 모든 메서드 타겟
	 *  execution(* abc..*.*(..)) 							>> abc패키지 및 하위 패키지에 있는 파라미터가 0개 이상인 메서드 타겟
	 *  execution(Long com.spring.aop.Boss.work(..))   		>> 리턴 타입인 Long인 com.spring.aop 패키지 안의 Boss클래스의 work 메서드 타겟
	 *  execution (* get*(*)) 								>> 이름이 get으로 시작하고 파라미터가 한 개인 메서드 타겟
	 *  execution(* get*(*,*)) 								>> 이름이 get으로 시작하고 파라미터가 2개인 메서드 타겟
	 *  execution(* read*(Integer,..)) 						>> 메서드 이름이 read 로시작하고, 첫번째 파라미터 타입이 Integer이며 한개 이상의 파라미터를 갖는 메서드 타겟
	 *  
	 * */
	
	
	private static Logger logger = LoggerFactory.getLogger(Advice.class);
	
	@Pointcut("execution(* work())") // 중복되는 메서드를 기술
	public void pointcut() {
		// 특정의미가 없다.
	}
	
	                   // 띄어쓰기중요 / 메서드 이름 상관 없음
	@Before("pointcut()")
	//@Before("execution(* work())")
	public void before() {
		logger.info("AOP Before 공통 메소드 호출 : 출근한다");
		//System.out.println("AOP Before 공통 메소드 호출 : 출근한다");
	}
	
	@After("pointcut()")
	//@After("execution(* work())")
	public void after() {
		logger.info("AOP After 공통 메소드 호출 : 퇴근한다.\n");
		//System.out.println("AOP After 공통 메소드 호출 : 퇴근한다.\n");
	}
	
	@Around("execution(* getWorkTime())")
	public void around(ProceedingJoinPoint pjp) {
		
		try {
			
			//메서드 호출전
			//System.out.println("\n===== 메서드 호출 전 =====");
			logger.info("\n===== 메서드 호출 전 =====");
			long startTime = System.currentTimeMillis();
			
			// 메서드 호출
			pjp.proceed(); // Real Subject의 사용 시점
		
			// 메서드 호출 후
			long endTime = System.currentTimeMillis();
			//System.out.println(" 업무 소요 시간 : " + (endTime - startTime));
			//System.out.println("===== 메서드 호출 전 =====\n");
			logger.info(" 업무 소요 시간 : " + (endTime - startTime));
			logger.info("===== 메서드 호출 전 =====\n");
		
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	// 호출된 메서드가 성공적으로 실행한 뒤 주입되는 기능
	                                 // (..) : 0개 시상 모든 파라미터(인트, 스트링 등등) 타켓 -> 모두 받기 
	@AfterReturning("execution(* getInfo(..))")
	public void afterReturning(JoinPoint jp) {
		                      // 넘어오는 파라미터를 받아주는 함수
//		System.out.println("1: " + Arrays.toString(jp.getArgs())); // *중요* 메서드의 파라미터를 확인!
//		System.out.println("2: " + jp.getKind()); // 메서드의 종류를 확인
//		System.out.println("3: " + jp.getSignature().getName()); // 어드바이즈 메서드에 대한 설명
//		System.out.println("4: " + jp.getTarget().toString()); // 대상 객체를 반환
//		System.out.println("5: " + jp.getThis().toString()); // 프록시 객체를 반환
//		System.out.println("AOP AfterReturning 공통 메서드 호출: 정상적으로 업무를 마무리 하였다.");
		logger.info("1 : " + Arrays.toString(jp.getArgs())); // 메서드의 파라미터를 확인
		logger.info("2 : " + jp.getKind()); 					// 메서드의 종류를 확인
		logger.info("3 : " + jp.getSignature().getName());   // 어드바이즈 메서드에 대한 설명
		logger.info("4 : " + jp.getTarget().toString());	    // 대상 객체를 반환
		logger.info("5 : " + jp.getThis().toString());       // 프록시 객체를 반환
		logger.info("AOP AfterReturning 공통 메서드 호출 : 정상적으로 업무를 마무리 하였다.\n");
	}
	
	// 호출된 메서드에서 에러가 발생한 경우 주입되는 기능
	@AfterThrowing("execution(void com.spring.aop.Employee.mistake())")
	public void afterThrowing() {
		//System.out.println("AOP AfterThrowing 공통 메서드 호출: 보고서를 잘 못 전송한 것을 이제서야 알았다.\n");
		logger.info("AOP AfterThrwoing 공통 메서드 호출 : 보고서를 잘 못 전송한 것을 이제서야 알았다.\n");
	}


}
