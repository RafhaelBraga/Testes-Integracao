package br.com.testeintegracao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.testeintegracao.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{

}
