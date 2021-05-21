package com.userquestion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;


import javax.persistence.CascadeType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long id;
   
   @Column(unique = true, nullable = false)
   private String email;

   @Column(unique = true, nullable = false)
   private String username;

   @Column(columnDefinition = "TEXT", nullable = false)
   private String password;

   private String firstName;

   private String lastName;

   @Temporal(TemporalType.TIMESTAMP)
   private Date creationDate;

   @Temporal(TemporalType.TIMESTAMP)
   private Date lastLoginStamp;

   @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
   @OrderBy(value = "id asc")
   private Set<Question> questions = new HashSet<>();
   
   //Utility Methods
   public void removeAllQuestions(User user) {
		for( Question q : this.questions ){
			q.getUsers().remove(user);
		    user.getQuestions().remove(this);
		}
   }
   
}