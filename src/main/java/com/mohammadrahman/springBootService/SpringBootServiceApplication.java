package com.mohammadrahman.springBootService;

import com.mohammadrahman.springBootService.controller.Library;
import com.mohammadrahman.springBootService.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
public class SpringBootServiceApplication {
	@Autowired
	LibraryRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(SpringBootServiceApplication.class, args);
	}
//	@Override
//	public void run(String[] args) {
//		Library lib = repository.findById("fc153").get();
//		System.out.println(lib.getAuthor());
//		Library entity = new Library();
//		entity.setAisle(321);
//		entity.setBook_name("Devops");
//		entity.setAuthor("Mohammad");
//		entity.setIsbn("bc1");
//		entity.setId("bc1321");
//
//		repository.save(entity);
//		List<Library>allRecords = repository.findAll();
//
//		for(Library item : allRecords) {
//			System.out.println(item.getBook_name());
//		}
//		repository.delete(entity);
//
//	}

}
