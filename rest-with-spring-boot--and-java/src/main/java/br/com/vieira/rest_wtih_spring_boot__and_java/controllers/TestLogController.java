package br.com.vieira.rest_wtih_spring_boot__and_java.controllers;

import br.com.vieira.rest_wtih_spring_boot__and_java.services.PersonServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestLogController {

    private Logger logger = LoggerFactory.getLogger(TestLogController.class.getName());

    @GetMapping("/test")
    public String testLog(){
        logger.info("This is an Info log");
        logger.warn("This is an Warn log");
        logger.debug("This is an Debug log");
        logger.error("This is an Error log");
        return "Logs generated sucessfully";
    }
}
