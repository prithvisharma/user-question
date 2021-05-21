package com.userquestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userquestion.entities.Question;
import com.userquestion.entities.User;

//Question Entity's Repository to perform database operations
public interface QuestionRepository extends JpaRepository<Question, Long> {
	
	Question findByQuestionText(String questionText);

}