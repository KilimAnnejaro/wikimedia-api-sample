package com.wikimedia.googleshortdescriptionapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GooglePersonService {

    public GooglePersonService() {
    }

    public PersonResponse getDescription(String name, String lang) throws IOException {
        try {
            URL nameQueryUrl = basicRawArticleURLGetter(name, lang);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(nameQueryUrl);
            // TODO: use a logging library
            System.out.printf("Raw response output for name %s and lang %s: %s%n", name, lang, json.get("query").get("pages").get(0));
            Pattern p = Pattern.compile("\\{\\{Short description\\|([A-Za-z0-9\\s]*)\\}\\}"); // get short description if it exists
            Matcher m = p.matcher(json.get("query").get("pages").get(0).toPrettyString()); // default to first entry
            if (m.find()) {
                return new PersonResponse(m.group(1), HttpStatus.OK);
            }
            return new PersonResponse(parseDescriptionForFirstSentence(name, lang), HttpStatus.OK);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return new PersonResponse("Person name given is not a proper string.", HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new PersonResponse("Failure to process JSON response.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
            return new PersonResponse("Failed to parse description for given name.", HttpStatus.BAD_REQUEST);
        }
    }

    // Construct the URL used for making the call to MediaWiki API.
    // TODO improve readability on URL by passing individual parameters into a URL constructor framework.
    protected URL basicRawArticleURLGetter(String name, String lang) throws MalformedURLException {
        // Input parsing to prevent an injection attack.
        // Regex looks for characters neither alphanumeric nor underscores.
        Pattern safetyPattern = Pattern.compile("[^a-zA-Z0-9_]");
        if (safetyPattern.matcher(name).find()) {
            throw new MalformedURLException();
        }

        // default language is English
        URL nameQueryUrl = new URL(String.format("https://en.wikipedia.org/w/api.php?action=query&prop=revisions&titles=%s&rvlimit=1&formatversion=2&format=json&rvprop=content", name));
        if (!Objects.isNull(lang)) {
            nameQueryUrl = new URL(String.format("https://%s.wikipedia.org/w/api.php?action=query&prop=revisions&titles=%s&rvlimit=1&formatversion=2&format=json&rvprop=content", lang, name));
        }
        return nameQueryUrl;
    }

    protected URL parsedTextRawUrlGetter(String name, String lang) throws MalformedURLException {
        // default language is English
        URL nameQueryUrl = new URL(String.format("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&exsentences=1&exlimit=1&titles=%s&explaintext=1&formatversion=2&format=json", name));
        if (!Objects.isNull(lang)) {
            nameQueryUrl = new URL(String.format("https://%s.wikipedia.org/w/api.php?action=query&prop=extracts&exsentences=1&exlimit=1&titles=%s&explaintext=1&formatversion=2&format=json", lang, name));
        }
        return nameQueryUrl;
    }

    protected String parseDescriptionForFirstSentence(String name, String lang) throws IOException {
        // no need to parse input here since this method is only called after basicRawArticleURLGetter, which pre-executes the input parsing
        URL nameQueryUrl = parsedTextRawUrlGetter(name, lang);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(nameQueryUrl);
        // TODO: use a logging library
        System.out.printf("Parsed response output for name %s and lang %s: %s%n", name, lang, json.get("query").get("pages").get(0));
        return json.get("query").get("pages").get(0).get("extract").toPrettyString();
    }
}
