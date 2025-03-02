package br.com.vieira.rest_wtih_spring_boot__and_java.services;

import br.com.vieira.rest_wtih_spring_boot__and_java.exception.ResourceNotFoundException;
import br.com.vieira.rest_wtih_spring_boot__and_java.model.Person;
import br.com.vieira.rest_wtih_spring_boot__and_java.repositoy.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public List<Person> findAll(){

        logger.info("Finding all People");
        return repository.findAll();

    }

    public Person findById(Long id){
        logger.info("Finding one People");

        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    }

    public Person create(Person person){
        logger.info("Creating one People");

       return  repository.save(person);

    }

    public Person update(Person person){
        logger.info("Updating one People");

        Person entity = findById(person.getId());
        entity.setFirstName(person.getFirstName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setLastName(person.getLastName());

      return repository.save(entity);


    }

    public void delete(Long id){
        logger.info("Delete one People");
        Person entity = findById(id);
        repository.delete(entity);
    }
}
