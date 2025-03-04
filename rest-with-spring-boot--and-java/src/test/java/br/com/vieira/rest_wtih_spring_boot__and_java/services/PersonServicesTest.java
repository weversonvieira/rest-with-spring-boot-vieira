package br.com.vieira.rest_wtih_spring_boot__and_java.services;

import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v1.PersonDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.mocks.MockPerson;
import br.com.vieira.rest_wtih_spring_boot__and_java.model.Person;
import br.com.vieira.rest_wtih_spring_boot__and_java.repositoy.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices services;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUp() {

        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Person> persons = input.mockEntityList();
        when(repository.findAll()).thenReturn(persons);
        List<PersonDTO> people = services.findAll();
        assertNotNull(people);
        assertEquals(14,people.size());
    }

    @Test
    void findById() {
        Person person = input.mockEntity(1);
        person.setId(1l);
        when(repository.findById(1l)).thenReturn(Optional.of(person));
        var result = services.findById(1l);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")
                && link.getHref().endsWith("/api/person/v1/1")
                && link.getType().equals("GET")));

        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("GET")));

        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("POST")));

        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("PUT")));

        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete")
                && link.getHref().endsWith("/api/person/v1")
                && link.getType().equals("DELETE")));

        assertEquals("Rua do Mercado",result.getAddress());
    }


    @Test
    void create() {

//        Person person = input.mockEntity(1);
//        person.setId(1l);
//        Person persisted = person;
//        persisted.setId(1l);
//
//        PersonDTO personDTO = input.mockDTO(1);
//        when(repository.save(person)).thenReturn(persisted);
//        var result = services.create(personDTO);
//        assertNotNull(result);
//        assertNotNull(result.getId());
//        assertNotNull(result.getLinks());
//
//        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")
//                && link.getHref().endsWith("/api/person/v1/1")
//                && link.getType().equals("GET")));
//
//        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("findAll")
//                && link.getHref().endsWith("/api/person/v1")
//                && link.getType().equals("GET")));
//
//        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("create")
//                && link.getHref().endsWith("/api/person/v1")
//                && link.getType().equals("POST")));
//
//        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("update")
//                && link.getHref().endsWith("/api/person/v1")
//                && link.getType().equals("PUT")));
//
//        assertNotNull(result.getLinks().stream().anyMatch(link -> link.getRel().value().equals("delete")
//                && link.getHref().endsWith("/api/person/v1")
//                && link.getType().equals("DELETE")));
//
//        assertEquals("Rua do Mercado",result.getAddress());
    }

    @Test
    void createV2() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        Person person = input.mockEntity(1);
        person.setId(1l);
        when(repository.findById(1l)).thenReturn(Optional.of(person));
        services.delete(1l);
        verify(repository,times(1)).findById(anyLong());
        verify(repository,times(1)).delete(any(Person.class));
        verifyNoMoreInteractions(repository);

    }
}