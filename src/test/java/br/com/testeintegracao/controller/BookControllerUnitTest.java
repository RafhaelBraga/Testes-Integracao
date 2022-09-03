package br.com.testeintegracao.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.testeintegracao.model.Book;
import br.com.testeintegracao.service.BookService;
import br.com.testeintegracao.utils.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerUnitTest {
	
	@Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;
    
    @After
    public void after() {
    	reset(bookService);
    }
    
    @Test
    public void givenValidBook_whenPostBook_thenCreateBook() throws Exception {
   
    	Book book = new Book(1, "testBook");

        given(bookService.create(Mockito.any())).willReturn(Optional.of(book));
        
        mvc.perform(post("/book")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(JsonUtil.toJson(book)))
        		.andExpect(status().isCreated())
        		.andExpect(jsonPath("$.title", is("testBook")));
        
        verify(bookService, VerificationModeFactory.times(1)).create(Mockito.any());
    }    
    
    @Test
    public void givenInvalidBook_whenPostBook_thenDontCreateBook() throws Exception {
    	
        given(bookService.create(Mockito.any())).willReturn(Optional.empty());

        mvc.perform(post("/book")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content("{\"fail\": \"a\"}"))
        		.andExpect(status().isBadRequest());
        verify(bookService, VerificationModeFactory.times(1)).create(Mockito.any());
    }   
    
    @Test
    public void givenABookList_whenGetBookList_thenBookListShouldBeReturned() throws Exception {

    	Book book01 = new Book("testBook01");
    	Book book02 = new Book("testBook02");
    	
    	List<Book> bookList = Arrays.asList(book01, book02);
    	
        given(bookService.getBooks()).willReturn(bookList);
        mvc.perform(get("/book")
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
      	      	.andExpect(jsonPath("$[0].title", is("testBook01")))
      	      	.andExpect(jsonPath("$[1].title", is("testBook02")));
        
        verify(bookService, VerificationModeFactory.times(1)).getBooks();
    } 
    
    @Test
    public void givenValidId_whenGetBookById_thenBookShouldBeReturned() throws Exception {

    	Book book01 = new Book(1, "testBook01");
    	
        given(bookService.getBookById(book01.getId())).willReturn(Optional.of(book01));
        
        mvc.perform(get("/book/{id}", book01.getId())
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
      	      	.andExpect(jsonPath("title", is(book01.getTitle())));
        
        verify(bookService, VerificationModeFactory.times(1)).getBookById(Mockito.anyInt());
    } 
    
    @Test
    public void givenInvalidId_whenGetBookById_thenBookShouldNotBeReturned() throws Exception {

        given(bookService.getBookById(1)).willReturn(Optional.empty());
        
        mvc.perform(get("/book/{id}", 1)
        		.contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isNoContent());
        
        verify(bookService, VerificationModeFactory.times(1)).getBookById(Mockito.anyInt());
    }
    
    @Test
    public void givenValidInput_whenPutBook_thenChangedBookShouldBeReturned() throws Exception {

    	Book book01 = new Book(1, "testBook01");
    	Book book01Modified = new Book(1, "testBook01Modified");


        given(bookService.getBookById(book01.getId())).willReturn(Optional.of(book01));
        given(bookService.modify(Mockito.any())).willReturn(book01Modified);
        
        mvc.perform(put("/book")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(JsonUtil.toJson(book01Modified)))        		
        		.andExpect(status().isOk())
        		.andExpect(jsonPath("$.id", is(book01Modified.getId())));
        
        verify(bookService, VerificationModeFactory.times(1)).modify(Mockito.any());
    }
    
    @Test
    public void givenInvalidInput_whenPutBook_thenChangedBookShouldBeReturned() throws Exception {

        given(bookService.getBookById(4)).willReturn(Optional.empty());
        
        mvc.perform(put("/book")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(JsonUtil.toJson(new Book())))        		
        		.andExpect(status().isNoContent())
        		.andExpect(content().string(""));
        
        verify(bookService, VerificationModeFactory.times(0)).modify(Mockito.any());
    }

    @Test
    public void givenValidInput_whenDeleteBook_thenStatus200() throws Exception {
    	Book bookToDelete = new Book(1, "deleteTest");
        given(bookService.getBookById(bookToDelete.getId())).willReturn(Optional.of(bookToDelete));
        
        mvc.perform(delete("/book/{id}", bookToDelete.getId())
        		.contentType(MediaType.APPLICATION_JSON))   		
        		.andExpect(status().isOk());
        
        verify(bookService, VerificationModeFactory.times(1)).delete(Mockito.anyInt());
    }
    
    @Test
    public void givenInvalidInput_whenDeleteBook_thenStatus204() throws Exception {

    	given(bookService.getBookById(1)).willReturn(Optional.empty());
        
        mvc.perform(delete("/book/{id}", 1)
        		.contentType(MediaType.APPLICATION_JSON))   		
        		.andExpect(status().isNoContent());
        
        verify(bookService, VerificationModeFactory.times(0)).delete(Mockito.anyInt());
    }
}