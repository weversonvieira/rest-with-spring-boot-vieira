package br.com.vieira.rest_wtih_spring_boot__and_java.services;

import br.com.vieira.rest_wtih_spring_boot__and_java.controllers.PersonController;
import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v1.PersonDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v2.PersonDTOV2;
import br.com.vieira.rest_wtih_spring_boot__and_java.exception.RequiredObjectIsNullException;
import br.com.vieira.rest_wtih_spring_boot__and_java.exception.ResourceNotFoundException;

import static br.com.vieira.rest_wtih_spring_boot__and_java.mapper.ObjectMapper.parseListObjects;
import static br.com.vieira.rest_wtih_spring_boot__and_java.mapper.ObjectMapper.parseObject;

import br.com.vieira.rest_wtih_spring_boot__and_java.mapper.custom.PersonMapper;
import br.com.vieira.rest_wtih_spring_boot__and_java.model.Person;
import br.com.vieira.rest_wtih_spring_boot__and_java.repositoy.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper converter;

    public List<PersonDTO> findAll() {

        logger.info("Finding all People");
        var persons = parseListObjects(repository.findAll(), PersonDTO.class);
        persons.forEach(this::addHateoasLinks);
        return persons;

    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one People");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }


    public PersonDTO create(PersonDTO person) {

        if(person == null){
            throw new RequiredObjectIsNullException();
        }
        logger.info("Creating one People");

        var entity = parseObject(person, Person.class);
        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTOV2 createV2(PersonDTOV2 person) {


        if(person == null){
            throw new RequiredObjectIsNullException();
        }
        logger.info("Creating one People v2");
        var entity = converter.convertDOTOEntity(person);
        return converter.convertEntityTODTO(repository.save(entity));

    }

    public PersonDTO update(PersonDTO person) {


        if(person == null){
            throw new RequiredObjectIsNullException();
        }
        logger.info("Updating one People");

        Person entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        entity.setFirstName(person.getFirstName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setLastName(person.getLastName());

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;


    }

    public void delete(Long id) {
        logger.info("Delete one People");
        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        repository.delete(entity);

    }

    private  void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
