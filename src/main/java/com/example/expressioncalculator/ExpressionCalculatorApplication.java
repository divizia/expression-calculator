package com.example.expressioncalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ExpressionCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpressionCalculatorApplication.class, args);
		runChrome();
	}

	public static void runChrome() {
		try {
			Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome http://localhost:8080"});
		} catch (IOException e) {
			
		}
	}

}
