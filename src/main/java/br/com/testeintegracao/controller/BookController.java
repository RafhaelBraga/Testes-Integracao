package br.com.testeintegracao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping("/create")
	public Book createBook(@RequestBody Book book) {
		return bookService.create(book);
	}
	
	@PutMapping("/put")
	public Book modifyBook(@RequestBody Book book) {
		return bookService.modify(book);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteBook(@PathVariable("id") int id) {
		bookService.delete(id);
	}

}
