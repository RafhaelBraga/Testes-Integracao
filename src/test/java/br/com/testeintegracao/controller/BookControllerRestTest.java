package br.com.testeintegracao.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.testeintegracao.model.Book;
import br.com.testeintegracao.repository.BookRepository;
import br.com.testeintegracao.utils.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc 
@AutoConfigureTestDatabase
public class BookControllerRestTest {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
    private MockMvc mvc;
	
	@After
    public void resetDb() {
		bookRepository.deleteAll();
    }
	
	@Test
    public void whenValidInput_thenCreateBook() throws IOException, Exception {
        Book book = new Book("TestCreate");
        mvc.perform(
        		post("/book").
        		contentType(MediaType.APPLICATION_JSON).
        		content(JsonUtil.toJson(book)));

        List<Book> found = bookRepository.findAll();
        assertThat(found).extracting(Book::getTitle).containsOnly("TestCreate");
    }
	
	@Test
    public void whenInvalidInput_thenStatus400AndDontCreateBook() throws IOException, Exception {
        mvc.perform(
        		post("/book")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content("{\"fail\": \"a\"}"))
        		.andExpect(status().is(400));

        List<Book> found = bookRepository.findAll();
        assertThat(found).extracting(Book::getTitle).isEmpty();
    }
	
	@Test
	public void givenBooks_whenGetBook_thenStatus200AndTitleIsRight() throws Exception {
		
		Book book = createTestBook("TestList01");
		
		mvc.perform(get("/book/{id}", book.getId())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("title", is(book.getTitle())));//.andDo(print())
	}
	
	@Test
	public void givenNoBooks_whenGetBookList_thenStatus204() throws Exception {
		
		mvc.perform(get("/book/{id}", 1)
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isNoContent())
  		  .andExpect(content().string(""));
	}
	
	@Test
	public void givenNoBooks_whenGetBookList_thenStatus200AndNoBooksFound() throws Exception {
		
		mvc.perform(get("/book")
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())
  		  .andExpect(content().string("[]"));
	}	
	
	@Test
	public void givenBooks_whenGetBookList_thenStatus200AndTitleIsRight() throws Exception {
		
		createTestBook("TestList01");
		createTestBook("TestList02");
	    
		mvc.perform(get("/book")
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())//.andDo(print())
	      .andExpect(jsonPath("$[0].title", is("TestList01")))
		  .andExpect(jsonPath("$[1].title", is("TestList02")));
	}
	
	@Test
	public void givenBooks_whenDeleteBookById_thenStatus200AndDeleteBook() throws Exception {
		
		Book book01 = createTestBook("TestBook01");
		
		@SuppressWarnings("unused")
		Book book02 = createTestBook("TestBook02");
		
		mvc.perform(delete("/book/{id}", book01.getId())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
		
		List<Book> found = bookRepository.findAll();
        assertThat(found).extracting(Book::getTitle).containsOnly("TestBook02");
	}
	
	@Test
	public void noGivenBooks_whenDeleteBookById_thenStatus204() throws Exception {
		
		
		mvc.perform(delete("/book/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());
		
		List<Book> found = bookRepository.findAll();
        assertThat(found).isEmpty();
	}
	
	@Test
	public void givenBooks_whenPutBook_thenStatus200AndModifyBook() throws Exception {
		
		Book book = createTestBook("TestBook01");
		book.setTitle("changedTitle");
		
		mvc.perform(put("/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(book)))
				.andExpect(status().isOk());

		List<Book> found = bookRepository.findAll();
        assertThat(found).extracting(Book::getTitle).containsOnly("changedTitle");
	}
	
	@Test
	public void givenInvalidInput_whenPutBook_thenStatus204() throws Exception {
		
		mvc.perform(put("/book")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(new Book("teste"))))
				.andExpect(status().isNoContent());

		List<Book> found = bookRepository.findAll();
        assertThat(found).isEmpty();
	}
	
	private Book createTestBook(String title) {
        Book book = new Book(title);
        return bookRepository.save(book);
    }

}
