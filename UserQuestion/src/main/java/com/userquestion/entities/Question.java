package com.userquestion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   @Column(columnDefinition = "TEXT")
   private String questionText;

   private String subject;

   private String chapter;

   private int marks;

   @Temporal(TemporalType.TIMESTAMP)
   private Date creationTimeStamp;

   @Temporal(TemporalType.TIMESTAMP)
   private Date editTimeStamp;

   // owner of resource_bank association table
   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(
           name = "resource_bank",
           joinColumns = @JoinColumn(name = "question_id"),
           inverseJoinColumns = @JoinColumn(name = "user_id"))
   private Set<User> users = new HashSet<>();
   
   //Utility Methods
   public void addUser(User userEntity) {
       this.users.add(userEntity);
       userEntity.getQuestions().add(this);
   }

   public void removeUser(User userEntity) {
       this.users.remove(userEntity);
       userEntity.getQuestions().remove(this);
   }
   
   public void removeAllUsers() {
       this.users.clear();
   }
}