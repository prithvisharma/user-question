package com.userquestion.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.userquestion.entities.Question;
import com.userquestion.entities.User;
import com.userquestion.repository.QuestionRepository;
import com.userquestion.repository.UserRepository;

//Main Controller, executes all the APIs
@RestController
public class MainController {
	
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	QuestionRepository questionRepository;
	
	//test api to add to a USER
	@PostMapping("/addtestu")
	public String addTestUser() {
		User user = new User();
		user.setEmail("prithvi@gmail.com");
		user.setUsername("prithvi2255");
		user.setPassword("123");
		user.setFirstName("prithvi");
		user.setLastName("sharma");
		user.setCreationDate(new Date());
		
		userRepository.save(user);
		
		return "added";
	}
	
	//displays users present in database
	@GetMapping("/display/users")
	public List <String> fetchUsers(){
		List <User> userList = new ArrayList<>();
		userList = userRepository.findAll();
		List <String> list = new ArrayList<>();
		for(User u : userList) {
			list.add(u.getFirstName()+" "+u.getLastName());
		}
		return list;
	}
	
	//adds new user to database
	@PostMapping("/save/user")
	public String saveUser(@RequestBody User apiuser){
		apiuser.setCreationDate(new Date()); 
		userRepository.save(apiuser);
		return "User Added";
	}
	
	//deletes user from database
	@PostMapping("/delete/user")
	public String deleteUser( @RequestBody String email){
		User user = new User();
		user = userRepository.findByEmail(email);
		user.removeAllQuestions(user);
		userRepository.delete(user);
		
		
		return "User Deleted";
	}
	
	//updates field of the user 
	@PutMapping("/update/user/{id}")
	public String updateUser(@PathVariable("id") long id, @RequestBody User apiUser) {
		Optional<User> user = userRepository.findById(id);
		User newUser = user.get();
		
		if(!(apiUser.getEmail()==null))	
			newUser.setEmail(apiUser.getEmail());
		
		if(!(apiUser.getFirstName()==null))	
			newUser.setFirstName(apiUser.getFirstName());
		
		if(!(apiUser.getLastName()==null))	
			newUser.setLastName(apiUser.getLastName());
		
		if(!(apiUser.getPassword()==null))	
			newUser.setPassword(apiUser.getPassword());
		
		if(!(apiUser.getUsername()==null))	
			newUser.setUsername(apiUser.getUsername());
		
		userRepository.save(newUser);
		return "User Updated";
	}
	
	//login api 
	@PostMapping("/auth")	
	public boolean login(@RequestBody String[] idpass) {
		User user = new User();
		boolean bool = userRepository.existsByEmail(idpass[0]);
		if(bool) {
			user = userRepository.findByEmail(idpass[0]);
			boolean bool2 ;
			bool2= user.getPassword().equals(idpass[1]);
			if(bool2) {
				user.setLastLoginStamp(new Date());
				userRepository.save(user);
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	//test api to add question
	@PostMapping("/addtestq")
	public String addTestQuestion() {
		Question question  = new Question();
		question.setQuestionText("What is API ?");
		question.setSubject("Computer Programming");
		question.setChapter("Application Communication");
		question.setMarks(5);
		question.setCreationTimeStamp(new Date());
				
		questionRepository.save(question);
		
		return "added";
	}
	
	//display all the question from database
	@GetMapping("/display/questions")
	public List <String> fetchQuestions(){
		List <Question> questionList = new ArrayList<>();
		questionList = questionRepository.findAll();
		List <String> list = new ArrayList<>();
		for(Question q : questionList) {
			list.add(q.getQuestionText()+" | "+q.getSubject()+" | "+q.getChapter()+" | "+q.getMarks());
		}
		return list;
	}
	
	//adds new question to database
	@PostMapping("/save/question")
	public String saveQuestion(@RequestBody Question apiquestion){
		apiquestion.setCreationTimeStamp(new Date());
		questionRepository.save(apiquestion);
		return "Question added";
	}
	
	//deletes question  from database
	@PostMapping("/delete/question")
	public String deleteQuestion( @RequestBody String questionText){
		Question question = new Question();
		question = questionRepository.findByQuestionText(questionText);
		question.removeAllUsers();
		questionRepository.delete(question);
		
		return "Question Deleted";
	}
	
	//updates field of question present in database
	@PutMapping("/update/question/{id}")
	public String updateQuestion(@PathVariable("id") long id, @RequestBody Question apiQuestion) {
		Optional<Question> question = questionRepository.findById(id);
		Question newQuestion = question.get();
		
		if(!(apiQuestion.getChapter()==null))
			newQuestion.setChapter(apiQuestion.getChapter());
				
		newQuestion.setEditTimeStamp(new Date());
		
		if(apiQuestion.getMarks()!=0L)
			newQuestion.setMarks(apiQuestion.getMarks());
		
		if(!(apiQuestion.getQuestionText()==null))
		newQuestion.setQuestionText(apiQuestion.getQuestionText());
		
		if(!(apiQuestion.getSubject()==null))
			newQuestion.setSubject(apiQuestion.getSubject());
		
		questionRepository.save(newQuestion);
		return "Question Updated";
	}
	
	//links question with the user
	@PostMapping("/link/questwithuser")
	public String linkQuestionWithUser(@RequestBody long[] linkingParametersQU) {
		Question question = new Question();
		question = questionRepository.getOne(linkingParametersQU[0]);
		User user = new User();
		user = userRepository.getOne(linkingParametersQU[1]);
		
		question.addUser(user);
		questionRepository.save(question);
		return "Question linked to user";
	}
	
	//display questions linked to a user
	@PostMapping("/display/userquestions")
	public List<String> getUserQuestions(@RequestBody String email ) {
		User user = new User();
		Set<Question> userQuestions = new HashSet<>();
		List<String> questions = new ArrayList<>();
		user = userRepository.findByEmail(email);
		userQuestions = user.getQuestions();
		for( Question q : userQuestions ){
            questions.add(q.getQuestionText());
        }
		return questions;
	}
	
	//displays users linked to a question
	@PostMapping("/display/questionusers")
	public List<String> getQuestionUsers(@RequestBody String questionText ) {
		User user = new User();
		Question question = new Question();
		Set<User> questionUsers = new HashSet<>();
		List<String> users = new ArrayList<>();
		question = questionRepository.findByQuestionText(questionText);
		questionUsers = question.getUsers();
		for( User u : questionUsers ){
			users.add(u.getUsername());
        }
		return users;
	}
	
	//shares a question to a user with its id
	@PostMapping("/sharetouser")
	public String shareToUser(@RequestBody long[] shareParameters) {
		Question question = new Question();
		question = questionRepository.getOne(shareParameters[0]);
		User shareUser = new User();
		shareUser = userRepository.getOne(shareParameters[1]);
		
		question.addUser(shareUser);
		questionRepository.save(question);
			    
		return "Shared";
	}
	
	//takes away grant of a question from a question
	@PostMapping("/disallowuser")
	public String disallowUser(@RequestBody long[] disallowParameters) {
		Question question = new Question();
		question = questionRepository.getOne(disallowParameters[0]);
		User disallowUser = new User();
		disallowUser = userRepository.getOne(disallowParameters[1]);
		
		question.removeUser(disallowUser);
		questionRepository.save(question);
		
		return "User Disallowed";
	}

}