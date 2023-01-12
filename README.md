# wikimedia-api-sample
Sample Java Spring Boot/Gradle persistent service calling the Wikipedia API.

## How to use
Clone this repository to local, cd into `google-short-description-api`, then use `./gradlew bootrun`.

## API input/output schemas and discussion of design decisions

### Input schema
```
personName: The name of the person for whom a description is sought. Required.
lang: Language of desired results.
```

### Output schema
```
A simple string contained exactly the description and nothing else.
```

### Design decisions
On handling a person not on English Wikipedia: he list of languages for Wikipedia is available at https://www.mediawiki.org/wiki/API:SetPageLanguage. Calls across the public internet take in the region of hundreds of milliseconds, so checking all the languages would take several seconds and be noticeable by the requester. Therefore, I’m opting to default to English and use a specific language if clearly requested.
