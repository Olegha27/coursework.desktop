package com.example.courseworkitfu.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("Client")
public class Client extends User{
 private String name;
 private String surname;
 private String cardNo;
 private int bonusPoints;
 private String address;
 private LocalDate dateOfBirth;
 private double balance;

 public String getName() { return name; }
 public void setName(String name) { this.name = name; }
 public String getSurname() { return surname; }
 public void setSurname(String surname) { this.surname = surname; }
 public String getCardNo() { return cardNo; }
 public void setCardNo(String cardNo) { this.cardNo = cardNo; }
 public int getBonusPoints() { return bonusPoints; }
 public void setBonusPoints(int bonusPoints) { this.bonusPoints = bonusPoints; }
 public String getAddress() { return address; }
 public void setAddress(String address) { this.address = address; }
 public LocalDate getDateOfBirth() { return dateOfBirth; }
 public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
 public double getBalance() { return balance; }
 public void setBalance(double balance) { this.balance = balance; }

 public Client(String username, String password, String phoneNum, String cardNo) {
 super(username, password, phoneNum);
 this.cardNo = cardNo;
 }

 public Client(String username, String password, String phoneNum, String cardNo, String address, LocalDate dateOfBirth) {
 super(username, password, phoneNum);
 this.cardNo = cardNo;
 this.address = address;
 this.dateOfBirth = dateOfBirth;
 }

 @Override
 public String renderUserData() {
 return "Sveikas kliente: " + name + " " + surname;
 }

}
