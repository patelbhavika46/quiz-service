# quiz-service

This microservice manages quizzes in the Quiz Application. It provides endpoints for creating, updating, and retrieving quizzes.

## Features

- CRUD operations for quizzes
- Integration with question-service
- RESTful API endpoints

## Tech Stack

- Java Spring Boot
- MySQL

## Getting Started

1. Configure your database in `application.properties`.
2. Build: `mvn clean install`
3. Run: `mvn spring-boot:run`
4. Service runs on default port (e.g., `8082`).

## API Endpoints

| Method | Endpoint      | Description                      |
|--------|---------------|----------------------------------|
| GET    | /quiz/{id}    | Get all Quiz question by quiz id |
| POST   | /quiz/submit/1  | submit quiz and get score        |
                    |
