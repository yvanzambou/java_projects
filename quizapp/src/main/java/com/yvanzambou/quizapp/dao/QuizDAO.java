package com.yvanzambou.quizapp.dao;

import com.yvanzambou.quizapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDAO extends JpaRepository<Quiz, Integer> {}
