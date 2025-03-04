package br.com.vieira.rest_wtih_spring_boot__and_java.controllers;

import br.com.vieira.rest_wtih_spring_boot__and_java.controllers.docs.BookControllerDocs;
import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v1.BookDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.services.BookServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing Book")
public class BookController implements BookControllerDocs {

    @Autowired
    BookServices service;


    @Override
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public List<BookDTO> findAll() {
        return service.findAll();

    }

    @Override
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public BookDTO findById(@PathVariable("id") Long id) {
        var book = service.findById(id);
        return book;

    }

    @Override
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public BookDTO create(@RequestBody BookDTO book) {

        return service.create(book);

    }



    @Override
    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    public BookDTO update(@RequestBody BookDTO book) {
        return service.update(book);

    }

    @Override
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();

    }
}
