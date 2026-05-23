# Parallel Web Crawler

A multithreaded web crawler built in Java using `ForkJoinPool`, dependency injection with Guice, and dynamic proxy-based profiling.

## Features

- Sequential and parallel web crawler implementations
- Parallel crawling using `ForkJoinPool`
- JSON configuration loading and result writing
- Word frequency analysis
- Dynamic proxy-based method profiler
- Thread-safe URL tracking
- Functional programming using Java Streams

## Technologies Used

- Java 17
- Maven
- Google Guice
- Jackson JSON Library
- JUnit 5

## Running Tests

```bash
mvn test
```

## Build Project

```bash
mvn package
```

## Run the Crawler

```bash
java -classpath target/udacity-webcrawler-1.0.jar com.udacity.webcrawler.main.WebCrawlerMain src/main/java/com/udacity/webcrawler/main/config/sample_config.json
```

## Sample Output

```json
{
  "wordCounts": {
    "library": 56,
    "books": 33,
    "book": 27
  },
  "urlsVisited": 4
}
```

## Project Structure

- `json/` → JSON configuration and result handling
- `parser/` → HTML page parsing
- `profiler/` → Dynamic proxy profiling system
- `main/` → Main application runner and configs

## Author

Harshit Chaurasia
