package com.mohammadrahman.springBootService.controller;

import com.mohammadrahman.springBootService.repository.LibraryRepository;
import com.mohammadrahman.springBootService.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController
public class LibraryController {
    @Autowired
    LibraryService service;
    @Autowired
    LibraryRepository repository;
    private static final Logger logger=  LoggerFactory.getLogger(LibraryController.class);

    @PostMapping("/addBook")
    public ResponseEntity addBookImplementation(@RequestBody Library library) {
      String id =  service.buildId(library.getIsbn(), library.getAisle()); // dependency mock
        AddResponse addResponse = new AddResponse();
        if(!service.checkBookAlreadyExist(id)) // mock
        {
            logger.info("Books do not exist so creating one");
            library.setId(id);
            repository.save(library); // mock
            HttpHeaders headers = new HttpHeaders();
            headers.add("unique", id);
            addResponse.setMsg("Success Book is Added");
            addResponse.setId(id);
            return new ResponseEntity<AddResponse>(addResponse, headers,HttpStatus.CREATED);
        } else {

            logger.info("Book exist so skipping creation");
            addResponse.setMsg("Books already exist");
            addResponse.setId(id);
            return new ResponseEntity<AddResponse>(addResponse,HttpStatus.ACCEPTED);
        }

    }
    @GetMapping("/getBooks/{id}")
    public Optional<Library> getBookById(@PathVariable(value="id") String id) {
        try {
            Optional<Library> lib = repository.findById(id);
            return lib;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping("getBooks/author")
    public List<Library> getBookByAuthorName(@RequestParam(value="authorname")String authorname) {
        return repository.findAllByAuthor(authorname);

    }
    @PutMapping("/updateBook/{id}")
    public ResponseEntity<Library> updateBook(@PathVariable(value="id") String id, @RequestBody Library library) {
         Library existingBook = repository.findById(id).get();
         existingBook.setAisle(library.getAisle());
         existingBook.setAuthor(library.getAuthor());
         existingBook.setBook_name(library.getBook_name());
         repository.save(existingBook);
         return new ResponseEntity<Library>(existingBook, HttpStatus.OK);

    }
    @DeleteMapping("/deleteBook")
    public ResponseEntity<String> deleteBookById(@RequestBody Library library) {
        try {
            Library libdelete = repository.findById(library.getId()).get();
            repository.delete(libdelete);
            logger.info("Book is deleted.");
            return new ResponseEntity<>("Book is deleted", HttpStatus.CREATED);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        }

    }
    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Library>> GetAllBooks() {
    List<Library> allBooks = repository.findAll();
    return  new ResponseEntity<>(allBooks, HttpStatus.OK);
    }
}
