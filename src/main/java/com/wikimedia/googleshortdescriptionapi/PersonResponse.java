package com.wikimedia.googleshortdescriptionapi;

import org.springframework.http.HttpStatus;

// TODO import framework to reduce boilerplate and improve code readability
public class PersonResponse {

    private String personDescription;

    private HttpStatus httpStatus;
    public PersonResponse(String personDescription, HttpStatus httpStatus) {
        this.personDescription = personDescription;
        this.httpStatus = httpStatus;
    }

    public String getPersonDescription() {
        return this.personDescription;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
