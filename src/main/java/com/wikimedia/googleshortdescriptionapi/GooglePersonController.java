package com.wikimedia.googleshortdescriptionapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class GooglePersonController {

    GooglePersonService googlePersonService = new GooglePersonService();

    // TODO
    // 3. Develop full test suite for whatever it is. (get here by Tues night).
    // 4. Write down to use bootrun, or maybe deploy on Cloud. Also respond to questions. (Weds)

    @GetMapping("/greeting")
    public ResponseEntity<String> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new ResponseEntity<>(String.format("Hello, %s", name), HttpStatus.OK);
    }

    @PostMapping("/google-person")
    public ResponseEntity<String> googlePerson(@RequestBody(required = false) PersonRequest personRequest) {
        if (personRequest.getPersonName() == null) {
            return new ResponseEntity<>("Malformed request received.", HttpStatus.BAD_REQUEST);
        }
        try {
            PersonResponse personResponse = googlePersonService.getDescription(personRequest.getPersonName(), personRequest.getLang());
            return new ResponseEntity<>(personResponse.getPersonDescription(), personResponse.getHttpStatus());
        } catch (IOException ex) {
            return new ResponseEntity<>("The person whose information you have requested does not have an article on Wikipedia.", HttpStatus.BAD_REQUEST);
        }
    }
}
