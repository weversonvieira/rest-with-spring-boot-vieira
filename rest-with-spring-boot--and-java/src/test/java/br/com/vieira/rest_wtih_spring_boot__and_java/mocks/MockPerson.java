package br.com.vieira.rest_wtih_spring_boot__and_java.mocks;

import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v1.PersonDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.model.Person;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setId(number.longValue());
        person.setGender("M");
        person.setAddress("Rua do Mercado");
        person.setFirstName("Weverson");
        person.setLastName("Vieira");
        return person;

    }

    public PersonDTO mockDTO(Integer number) {
        PersonDTO person = new PersonDTO();
        person.setId(number.longValue());
        person.setGender("M");
        person.setAddress("Rua do Mercado");
        person.setFirstName("Weverson");
        person.setLastName("Vieira");
        return person;

    }

    public List<Person> mockEntityList(){
        List<Person> persons = new ArrayList<>();
        for(int i = 0; i < 14; i++){
            persons.add(mockEntity(i));
        }

        return persons;
    }

    public List<PersonDTO> mockDTOList(){
        List<PersonDTO> persons = new ArrayList<>();
        for(int i = 0; i < 14; i++){
            persons.add(mockDTO(i));
        }

        return persons;
    }

}
