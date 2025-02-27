package com.yvanzambou.quizapp.service;

import com.yvanzambou.quizapp.dao.QuestionDAO;
import com.yvanzambou.quizapp.dao.QuizDAO;
import com.yvanzambou.quizapp.model.QuestionWrapper;
import com.yvanzambou.quizapp.model.Quiz;
import com.yvanzambou.quizapp.model.Question;
import com.yvanzambou.quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDAO quizDao;
    @Autowired
    QuestionDAO questionDao;

    public ResponseEntity<String> createQuiz(String category, int numberOfQuestion) {

        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numberOfQuestion);

        Quiz quiz = new Quiz();
        quiz.setTitle(category);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz =  quizDao.findById(id);
        List<Question> questionsFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionsForUser = new ArrayList<>();

        for (Question q: questionsFromDB) {
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4(), q.getQuestionTitle());
            questionsForUser.add(qw);
        }
        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();
        int right = 0;
        int i = 0;
        for (Response resp: responses) {
            if (resp.getResponse().equals(questions.get(i).getRightAnswer()))
                right++;
            i++;
        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }
}
