package com.project.MALocacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MaLocacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaLocacaoApplication.class, args);
	}
	@GetMapping("/index")
	public String index(){
		return "Olá Mundo!";
	} 
}
