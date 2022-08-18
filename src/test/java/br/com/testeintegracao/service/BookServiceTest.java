package br.com.testeintegracao.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.testeintegracao.model.Book;
import br.com.testeintegracao.repository.BookRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookServiceTest {
	
	@MockBean
	private BookRepository bookRepository;
	
	@Autowired
	private BookService bookService;
	
	Book book01;
	Book book02;
	Book book03;
	
	@Before
	public void setup() {
		
		book01 = new Book("book01");
		book02 = new Book("book02");
		book03 = new Book("book03");
		
		List<Book> allBooks = Arrays.asList(book01, book02, book03);
		
		Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book01));
		Mockito.when(bookRepository.findById(2)).thenReturn(Optional.of(book02));
		Mockito.when(bookRepository.findById(3)).thenReturn(Optional.of(book03));
		Mockito.when(bookRepository.findById(4)).thenReturn(Optional.empty());
		Mockito.when(bookRepository.save(book01)).thenReturn(book01);
        Mockito.when(bookRepository.findAll()).thenReturn(allBooks);
	}
	
	@Test
    public void whenValidId_thenBookShouldBeFound() {
        Optional<Book> found = bookService.getBookById(1);
        assertThat(found.get().getTitle()).isEqualTo("book01");
    }

	@Test
    public void whenInvalidId_thenBookShouldNotBeFound() {
        Optional<Book> found = bookService.getBookById(4);
        assertThat(found).isEmpty();
    }
	
	@Test
    public void whenGetAllBooks_thenAllBookShouldBeFound() {
        List<Book> found = bookService.getBooks();
        
        Mockito.verify(bookRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(bookRepository);

        assertThat(found).hasSize(3).extracting(Book::getTitle).contains(book01.getTitle(), book02.getTitle(), book03.getTitle());
    }
	
	@Test
    public void whenCreateBook_thenBookShouldBeCreated() {

		Book created = bookRepository.save(book01);
        
        Mockito.verify(bookRepository, VerificationModeFactory.times(1)).save(Mockito.any());
        Mockito.reset(bookRepository);

        assertThat(created.getTitle()).isEqualTo(book01.getTitle());
    }	
	
	@Test
    public void whenModifyBook_thenBookShouldBeModified() {
		
		Book found = bookService.getBookById(1).get();
		found.setTitle("modified");
		
		found = bookService.modify(found);
		  
        Mockito.verify(bookRepository, VerificationModeFactory.times(1)).save(Mockito.any());
        Mockito.reset(bookRepository);

        assertThat(found.getTitle()).isEqualTo(book01.getTitle());
    }	
	
	
	@Test
    public void whenDeleteBook_thenBookShouldBeDeleted() {

		bookService.delete(book01.getId());
		Optional<Book> found = bookService.getBookById(book01.getId());

        Mockito.verify(bookRepository, VerificationModeFactory.times(1)).deleteById(Mockito.any());
        assertThat(found).isEmpty();
    }	
	
}
