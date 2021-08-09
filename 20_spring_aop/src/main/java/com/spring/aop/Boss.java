package com.spring.aop;

import org.springframework.stereotype.Component;

//테스트 목적의 클래스1
@Component
public class Boss {
	
	public void work() {
		System.out.println("사장의 업무를 본다.");
	}
	
	public void getWorkTime() {
		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void getInfo(String title , int salary) {
		System.out.println("직급: " + title);
		System.out.println("급여: " + salary);
	}
}
