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
- On handling a person not on English Wikipedia: the list of languages for Wikipedia is available at https://www.mediawiki.org/wiki/API:SetPageLanguage. Calls across the public internet take in the region of hundreds of milliseconds, so checking all the languages would take several seconds and be noticeable by the requester. Therefore, I’m opting to default to English and use a specific language if clearly requested. If the person isn't on English Wikipedia, that should result in a graceful fail.

- On handling an article not containing a short description: I used a different API, <https://www.mediawiki.org/wiki/Extension:TextExtracts#API>, and extracted the first sentence of the article. I wasn't sure if a library like OpenNLP had the capability to generate an accurate automatic summary, so I opted to go with something safer.

- I assumed that the first result of a search was likely to be the most accurate.

- The user is forced to provide an accurately spelled and correctly formatted name (like `Helen_Keller`, not `Helen Keller` or `HelenKeller`). Guessing what a user intended a name to be is a hard problem and not in scope for this task.

- There is basic code-level support to parse input for safety from injection attacks.

## Keeping the service highly functional and available

- The service needs a proxy in front of it to protect from DDoS attacks.
- The service is expected to be used globally by millions of customers with a very high QPS. As such, the service needs a load balancer to ensure that traffic is equally distributed among a global network of data centers.
- The service would benefit from a cache to store frequent requests from customers. Since people-related queries are often time-related (eg, "Justin Bieber" queries increase whenever a new album drops), it might make sense to use a LRU (least recently used) cache eviction strategy.
- The service is designed to query the Wikipedia API in response to all queries. It might make sense to instead copy all the data on Wikipedia into a database/storage system closer to the API's servers, updating as need be (the update system would have to be real-time, since a batch processing approach just isn't fast enough to reflect fast-changing current events related to a person).
