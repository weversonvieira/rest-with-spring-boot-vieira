package br.com.vieira.rest_wtih_spring_boot__and_java.mapper.custom;

import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v2.PersonDTOV2;
import br.com.vieira.rest_wtih_spring_boot__and_java.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityTODTO(Person person){
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(person.getId());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setBirthDay(new Date());
        return dto;

    }

    public Person convertDOTOEntity(PersonDTOV2 person){
        Person dto = new Person();
        dto.setId(person.getId());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        return dto;

    }
}
