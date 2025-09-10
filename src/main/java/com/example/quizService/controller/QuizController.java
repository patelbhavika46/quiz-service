package com.example.quizService.controller;

import com.example.quizService.entity.QuestionWrapper;
import com.example.quizService.entity.Quiz;
import com.example.quizService.entity.QuizDto;
import com.example.quizService.entity.Response;
import com.example.quizService.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    // Creates a new quiz based on category, number of questions, and title.
    @PostMapping("create")
    public ResponseEntity<Quiz> createQuiz(@RequestBody QuizDto quizDto) {
        Quiz newQuiz = quizService.createQuiz(quizDto.getCategoryName(), quizDto.getNumQuestions(), quizDto.getTitle());

        // Build the URI for the newly created resource
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newQuiz.getId())
                .toUri();

        return ResponseEntity.created(location).body(newQuiz);
    }

    // Retrieves questions for a specific quiz by ID.
    @GetMapping("/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id) {
        List<QuestionWrapper> questions = quizService.getQuizQuestions(id);
        return ResponseEntity.ok(questions);
    }

    // Submits a quiz and returns the user's score.
    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id, @RequestBody List<Response> responses) {
        Integer score = quizService.calculateResult(id, responses);
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
}
