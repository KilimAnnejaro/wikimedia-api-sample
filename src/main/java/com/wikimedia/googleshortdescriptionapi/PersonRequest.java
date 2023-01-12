package com.wikimedia.googleshortdescriptionapi;

public class PersonRequest {
    private String personName;
    private String lang;

    public PersonRequest() {
    }

    public PersonRequest(String personName) {
        this.personName = personName;
        this.lang = "";
    }

    public PersonRequest(String personName, String lang) {
        this.personName = personName;
        this.lang = lang;
    }

    public String getPersonName() {
        return personName;
    }

    public String getLang() {
        return lang;
    }
}
