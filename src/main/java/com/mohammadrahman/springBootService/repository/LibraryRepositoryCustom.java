package com.mohammadrahman.springBootService.repository;

import com.mohammadrahman.springBootService.controller.Library;

import java.util.List;

public interface LibraryRepositoryCustom {
     List<Library> findAllByAuthor(String authorName);



}
