package br.com.testeintegracao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.testeintegracao.model.Book;
import br.com.testeintegracao.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}
	
	public Optional<Book> getBookById(int id) {
		return bookRepository.findById(id);
	}
	
	public Optional<Book> create(Book book) {
		try {
			Book newBook = bookRepository.save(book);
			return Optional.of(newBook);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public Book modify(Book book) {
		return bookRepository.save(book);
	}
	
	public void delete(int id) {
		bookRepository.deleteById(id);
	}
}
