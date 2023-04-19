package com.mohammadrahman.springBootService;

import static org.hamcrest.CoreMatchers.is;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mohammadrahman.springBootService.controller.AddResponse;
import com.mohammadrahman.springBootService.controller.Library;
import com.mohammadrahman.springBootService.controller.LibraryController;
import com.mohammadrahman.springBootService.repository.LibraryRepository;
import com.mohammadrahman.springBootService.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootServiceApplicationTests {
  @Autowired LibraryController con;

  @MockBean LibraryRepository repository;
  @MockBean LibraryService libraryService;
  @Autowired private MockMvc mockMvc;

  @Test
  void contextLoads() {}

  @Test
  public void checkBuildIDLogic() {
    LibraryService lib = new LibraryService();
    String id = lib.buildId("ZAMAN", 24);
    assertEquals(id, "OLDZAMAN24");
    String id1 = lib.buildId("MAN", 24);
    assertEquals(id1, "MAN24");
  }

  @Test
  public void addBook() {
    // mock
    Library lib = buildLibrary();
    when(libraryService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
    when(libraryService.checkBookAlreadyExist(lib.getId())).thenReturn(true);
    when(repository.save(any())).thenReturn(lib);

    ResponseEntity response = con.addBookImplementation(buildLibrary());
    System.out.println(response.getStatusCode());
    assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED);
    AddResponse ad = (AddResponse) response.getBody();
    ad.getId();
    assertEquals(lib.getId(), ad.getId());
    assertEquals("Books already exist", ad.getMsg());
  }

  @Test
  public void addBookControllerTest() throws Exception {
    Library lib = buildLibrary();
    ObjectMapper map = new ObjectMapper();
    String jsonString = map.writeValueAsString(lib);
    when(libraryService.buildId(lib.getIsbn(), lib.getAisle())).thenReturn(lib.getId());
    when(libraryService.checkBookAlreadyExist(lib.getId())).thenReturn(false);
    when(repository.save(any())).thenReturn(lib);
    this.mockMvc
        .perform(post("/addBook").contentType(MediaType.APPLICATION_JSON).content(jsonString))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(lib.getId()));
  }

  @Test
  public void getBookByAuthorTest() throws Exception {
    List<Library> li = new ArrayList<>();
    li.add(buildLibrary());
    li.add(buildLibrary());

    when(repository.findAllByAuthor(any())).thenReturn(li);
    this.mockMvc
        .perform(get("/getBooks/author").param("authorname", "Mohammad Rahman"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(2)))
        .andExpect(jsonPath("$.[0].id").value("fcn322"));
  }

  @Test
  public void updateBookTest() throws Exception {
    Library lib = buildLibrary();
    ObjectMapper map = new ObjectMapper();
    String jsonString = map.writeValueAsString(UpdateLibrary());
    when(libraryService.getBookById(any())).thenReturn(buildLibrary());
    this.mockMvc
        .perform(
            put("/updateBook/" + lib.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    "{\"book_name\":\"Boot\",\"id\":\"fcm321\",\"isbn\":\"fcm\",\"aisle\":321,\"author\":\"Nuhaa Rahman\"}"));
  }

  @Test
  public void deleteBookControllerTest() throws Exception {
    when(libraryService.getBookById(any())).thenReturn(buildLibrary());
    doNothing().when(repository).delete(buildLibrary());
    this.mockMvc
        .perform(
            delete("/deleteBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \"fcn322\"}"))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string("Book is deleted"));
  }

  public Library buildLibrary() {
    Library lib = new Library();
    lib.setAisle(322);
    lib.setBook_name("Engineering");
    lib.setIsbn("fcn");
    lib.setAuthor("Yusuf Rahman");
    lib.setId("fcn322");
    return lib;
  }

  public Library UpdateLibrary() {
    Library lib = new Library();
    lib.setAisle(321);
    lib.setBook_name("Boot");
    lib.setIsbn("fcm");
    lib.setAuthor("Nuhaa Rahman");
    lib.setId("fcm321");
    return lib;
  }
}
