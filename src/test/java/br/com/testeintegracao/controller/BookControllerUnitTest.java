package br.com.testeintegracao.controller;

import static org.mockito.Mockito.reset;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import br.com.testeintegracao.model.Book;
import br.com.testeintegracao.service.BookService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
    public void whenPostEmployee_thenCreateEmployee() throws Exception {
   
    	Book book = new Book("testBook");
//   
//        given(bookService.create(Mockito.any())).willReturn(book);
//        mvc.perform(post("/book/create").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(book))).andExpect(status().isCreated()).andExpect(jsonPath("$.title", is("testBook")));
//        verify(bookService, VerificationModeFactory.times(1)).create(Mockito.any());
    }    
    
}