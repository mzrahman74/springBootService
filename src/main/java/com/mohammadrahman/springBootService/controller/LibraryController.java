package com.mohammadrahman.springBootService.controller;

import com.mohammadrahman.springBootService.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LibraryController {
    @Autowired
    LibraryRepository repository;

    @PostMapping("/addBook")
    public ResponseEntity addBookImplementation(@RequestBody Library library) {
        AddResponse addResponse = new AddResponse();
        String id = library.getIsbn()+library.getAisle();
        library.setId(id);
        repository.save(library);
        HttpHeaders headers = new HttpHeaders();
        headers.add("unique", id);
        addResponse.setMsg("Success Book is Added");
        addResponse.setId(id);
        return new ResponseEntity<AddResponse>(addResponse,headers,HttpStatus.CREATED);

    }
}
