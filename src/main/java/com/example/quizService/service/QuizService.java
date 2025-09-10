package com.example.quizService.service;

import com.example.quizService.dao.QuizDao;
import com.example.quizService.entity.QuestionWrapper;
import com.example.quizService.entity.Quiz;
import com.example.quizService.entity.Response;
import com.example.quizService.feign.QuizInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for handling quiz-related business logic.
 * It interacts with the QuizDao and QuestionDao to perform operations.
 */
@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    /**
     * Creates a new quiz by selecting random questions from a given category.
     * @param category The category of questions to include.
     * @param numQ The number of questions to include.
     * @param title The title of the quiz.
     * @return The created Quiz entity.
     * @throws ResponseStatusException if no questions are found for the given category.
     */
    @Transactional
    public Quiz createQuiz(String category, int numQ, String title) {
        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No questions found for the given category.");
        }
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        return quizDao.save(quiz);
    }

    /**
     * Retrieves a quiz by its ID and returns a list of questions for the user.
     * The correct answers are not included in the response.
     * @param id The ID of the quiz to retrieve.
     * @return A list of QuestionWrapper objects.
     * @throws ResponseStatusException if the quiz is not found.
     */
    public List<QuestionWrapper> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();

        List<QuestionWrapper> questions = quizInterface.getQuestionsFromId(questionIds).getBody();

        return questions;
    }

    /**
     * Calculates the score of a submitted quiz.
     * @param id The ID of the quiz.
     * @param responses The user's list of responses.
     * @return The total score.
     * @throws ResponseStatusException if the quiz is not found.
     */
    @Transactional
    public Integer calculateResult(Integer id, List<Response> responses) {

        Integer score = quizInterface.getScore(responses).getBody();
        return score;
    }
}
