package com.example.courseworkitfu.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
//@MappedSuperclass
public class User {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 protected int id;
//@Column(length = 30) if you wish to control varchar length
 @Column(unique = true)
 protected String username;
 protected String password;
 protected String email;
 protected LocalDateTime dateCreated;
 protected boolean isAdmin;
 protected boolean isActive;
 protected String phoneNum;

 public int getId() { return id; }
 public void setId(int id) { this.id = id; }
 public String getUsername() { return username; }
 public void setUsername(String username) { this.username = username; }
 public String getPassword() { return password; }
 public void setPassword(String password) { this.password = password; }
 public String getEmail() { return email; }
 public void setEmail(String email) { this.email = email; }
 public LocalDateTime getDateCreated() { return dateCreated; }
 public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }
 public boolean isAdmin() { return isAdmin; }
 public void setAdmin(boolean admin) { isAdmin = admin; }
 public boolean isActive() { return isActive; }
 public void setActive(boolean active) { isActive = active; }
 public String getPhoneNum() { return phoneNum; }
 public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }

 public User(String username, String password) {
 this.username = username;
 this.password = password;
 this.isActive = true;
 }

 public User(String username, String password, String phoneNum) {
 this.username = username;
 this.password = password;
 this.phoneNum = phoneNum;
 this.isActive = true;
 }

 public User(String username, String password, String phoneNum, String email) {
 this.username = username;
 this.password = password;
 this.phoneNum = phoneNum;
 this.email = email;
 this.isActive = true;
 }


//Overloading - same method name, but different parameters
 public String renderUserData() {
 return "Current user: " + username;
 }
 public String renderUserData(String message){
 return message + " " + username;
 }

//Override - same method name as parent class, but with different implementation
 @Override
 public String toString() {
 return "User{" +
 "id=" + id +
 ", username='" + username + '\'' +
 ", password='" + password + '\'' +
 ", dateCreated=" + dateCreated +
 ", isAdmin=" + isAdmin +
 ", email='" + email + '\'' +
 ", isActive=" + isActive +
 ", phoneNum='" + phoneNum + '\'' +
 '}';
 }
}
