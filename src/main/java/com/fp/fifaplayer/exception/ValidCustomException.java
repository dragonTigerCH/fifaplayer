package com.fp.fifaplayer.exception;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidCustomException extends RuntimeException{

    private Error[] errors;

    public ValidCustomException(String defaultMessage, String field){
        this.errors = new Error[]{new Error(defaultMessage, field)};
    }

    public ValidCustomException(Error[] errors) {
        this.errors = errors;
    }

    public Error[] getErrors() {
        return errors;
    }

    public static class Error {

        private String defaultMessage;
        private String field;

        public Error(String defaultMessage, String field) {
            this.defaultMessage = defaultMessage;
            this.field = field;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public String getField() {
            return field;
        }
    }






}

