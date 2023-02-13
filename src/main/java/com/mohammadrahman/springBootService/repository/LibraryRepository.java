package com.mohammadrahman.springBootService.repository;

import com.mohammadrahman.springBootService.controller.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, String> {

}
