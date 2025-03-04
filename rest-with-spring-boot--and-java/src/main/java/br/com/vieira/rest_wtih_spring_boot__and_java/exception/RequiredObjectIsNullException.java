package br.com.vieira.rest_wtih_spring_boot__and_java.exception;

public class RequiredObjectIsNullException extends RuntimeException{

    public RequiredObjectIsNullException(){
        super("It is not allowed to persist a null object!");
    }

    public RequiredObjectIsNullException(String message){
        super(message);
    }
}
