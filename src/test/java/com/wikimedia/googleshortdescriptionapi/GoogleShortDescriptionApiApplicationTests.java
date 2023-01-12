package com.wikimedia.googleshortdescriptionapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class GoogleShortDescriptionApiApplicationTests {

    GooglePersonService googlePersonService = new GooglePersonService();

    @Test()
    void dangerousInputReturnsError() throws IOException {

        assertThrows(IOException.class, () -> {
            googlePersonService.basicRawArticleURLGetter("aaa%6", "de");
        });
    }

    @Test
    void urlChangesWithLanguage() throws MalformedURLException {
        assertNotEquals(googlePersonService.basicRawArticleURLGetter("Helen_Keller",""),googlePersonService.basicRawArticleURLGetter("Helen_Keller","de"));
        assertNotEquals(googlePersonService.parsedTextRawUrlGetter("Helen_Keller",""),googlePersonService.parsedTextRawUrlGetter("Helen_Keller","de"));
    }

    // TODO add integration tests for full functionality
    // TODO add testing for REST controller

}
