package com.spring.aop;

import org.springframework.stereotype.Component;

//테스트 목적의 클래스3
@Component
public class Employee {
	
	public void work() {
		System.out.println("직원의 업무를 본다.");
	}
	
	public void getWorkTime() {
		
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void getInfo(String title , int salary) {
		System.out.println("직급: " + title);
		System.out.println("급여: " + salary);
	}
	
	public void mistake() {
		System.out.println("실수로 보고서를 다른 상사에게 보냈다");
		System.out.println(3/0); // error 발생
	}
}
