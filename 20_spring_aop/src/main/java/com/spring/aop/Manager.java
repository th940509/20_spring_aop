package com.spring.aop;

import org.springframework.stereotype.Component;

//테스트 목적의 클래스2
@Component
public class Manager {
	
	public void work() {
		System.out.println("관리자의 업무를 본다.");
	}
	
	public void getWorkTime() {
		
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void getInfo(String title , int salary) {
		System.out.println("직급: " + title);
		System.out.println("급여: " + salary);
	}
}
