package br.com.testeintegracao.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.testeintegracao.model.Book;
import br.com.testeintegracao.service.BookService;

@RestController
@RequestMapping("/book")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@GetMapping("/list")
	public List<Book> getBookList() {
		return bookService.getBooks();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Book> getBook(@PathVariable int id) {
		Optional<Book> book = bookService.getBookById(id);
		if(book.isPresent()) {
			return ResponseEntity.ok().body(book.get());
		}
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/create")
	public ResponseEntity<Book> createBook(@RequestBody Book book, UriComponentsBuilder uriBuilder) {		
		Optional<Book> newBook = bookService.create(book);		
		if(newBook.isPresent()) {
			URI uri = uriBuilder.path("/book/{id}").buildAndExpand(newBook.get().getId()).toUri();
			return ResponseEntity.created(uri).body(newBook.get());			
		}
		return ResponseEntity.badRequest().build();		
	}
	
	@PutMapping("/put")
	public ResponseEntity<Book> modifyBook(@RequestBody Book updatedBook) {
		Optional<Book> bookToUpdate = bookService.getBookById(updatedBook.getId());
        if(bookToUpdate.isPresent()) {
        	Book modifiedBook = bookService.modify(updatedBook);
        	return ResponseEntity.ok().body(modifiedBook);
        }
        return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Book> deleteBook(@PathVariable("id") int id) {
		Optional<Book> bookToDelete = bookService.getBookById(id);
		if(bookToDelete.isPresent()) {
			bookService.delete(id);
			return ResponseEntity.ok().build();
		}
        return ResponseEntity.noContent().build();
	}

}
