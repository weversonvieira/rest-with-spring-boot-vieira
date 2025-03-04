package br.com.vieira.rest_wtih_spring_boot__and_java.repositoy;

import br.com.vieira.rest_wtih_spring_boot__and_java.model.Book;
import br.com.vieira.rest_wtih_spring_boot__and_java.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
}
