package com.spring.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
	
	@Autowired
	private Boss boss;
	
	@Autowired
	private Manager manager;
	
	@Autowired
	private Employee employee;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String main() {
		
		boss.work();
		manager.work();
		employee.work();
		
		System.out.println("\n----------------------------------\n");
		
		boss.getWorkTime();
		manager.getWorkTime();
		employee.getWorkTime();
		
		System.out.println("\n----------------------------------\n");
		
		boss.getInfo("사장" , 1200);
		manager.getInfo("매니저", 700);
		employee.getInfo("직원", 300);

		System.out.println("\n----------------------------------\n");
		
		//employee.mistake();
		
		return "home";
	}
}
