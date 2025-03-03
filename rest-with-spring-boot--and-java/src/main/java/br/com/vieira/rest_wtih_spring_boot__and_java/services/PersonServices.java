package br.com.vieira.rest_wtih_spring_boot__and_java.services;

import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v1.PersonDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v2.PersonDTOV2;
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
        return parseListObjects(repository.findAll(), PersonDTO.class);

    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one People");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return parseObject(entity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating one People");

        var entity = parseObject(person, Person.class);
        return parseObject(repository.save(entity), PersonDTO.class);

    }

    public PersonDTOV2 createV2(PersonDTOV2 person) {
        logger.info("Creating one People v2");

        var entity = converter.convertDOTOEntity(person);
        return converter.convertEntityTODTO(repository.save(entity));

    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating one People");

        Person entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        entity.setFirstName(person.getFirstName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setLastName(person.getLastName());

        return parseObject(repository.save(entity),PersonDTO.class);


    }

    public void delete(Long id) {
        logger.info("Delete one People");
        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        repository.delete(entity);
    }
}
