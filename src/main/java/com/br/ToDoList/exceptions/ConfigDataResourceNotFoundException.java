package com.br.ToDoList.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConfigDataResourceNotFoundException extends RuntimeException {


    public ConfigDataResourceNotFoundException(String msg){
        super(msg);
    }
}
