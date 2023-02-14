package com.mohammadrahman.springBootService.controller;

import com.mohammadrahman.springBootService.repository.LibraryRepository;
import com.mohammadrahman.springBootService.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
public class LibraryController {
    @Autowired
    LibraryService service;
    @Autowired
    LibraryRepository repository;
    private static final Logger logger=  LoggerFactory.getLogger(LibraryController.class);

    @PostMapping("/addBook")
    public ResponseEntity addBookImplementation(@RequestBody Library library) {
      String id =  service.buildId(library.getIsbn(), library.getAisle());
        AddResponse addResponse = new AddResponse();
        if(!service.checkBookAlreadyExist(id)) {
            logger.info("Books do not exist so creating one");
            library.setId(id);
            repository.save(library);
            HttpHeaders headers = new HttpHeaders();
            headers.add("unique", id);
            addResponse.setMsg("Success Book is Added");
            addResponse.setId(id);
            return new ResponseEntity<AddResponse>(addResponse, headers,HttpStatus.CREATED);
        } else {

            logger.info("Book exist so skipping creation");
            addResponse.setId("Books already exist");
            addResponse.setId(id);
            return new ResponseEntity<AddResponse>(addResponse,HttpStatus.ACCEPTED);
        }

    }
}
