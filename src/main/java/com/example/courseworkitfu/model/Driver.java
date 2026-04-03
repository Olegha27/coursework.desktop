package com.example.courseworkitfu.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@DiscriminatorValue("Driver")
public class Driver extends User{
 private String name;
 private String surname;
 private String driverLicence;
 @Enumerated(EnumType.STRING)
 private VehicleType vehicleType;
 private LocalDate birthDate;
 private boolean isAvailable;
 private double rating;
 private int totalDeliveries;
 private String vehiclePlateNumber;

 public String getName() { return name; }
 public void setName(String name) { this.name = name; }
 public String getSurname() { return surname; }
 public void setSurname(String surname) { this.surname = surname; }
 public String getDriverLicence() { return driverLicence; }
 public void setDriverLicence(String driverLicence) { this.driverLicence = driverLicence; }
 public VehicleType getVehicleType() { return vehicleType; }
 public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
 public LocalDate getBirthDate() { return birthDate; }
 public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
 public boolean isAvailable() { return isAvailable; }
 public void setAvailable(boolean available) { isAvailable = available; }
 public double getRating() { return rating; }
 public void setRating(double rating) { this.rating = rating; }
 public int getTotalDeliveries() { return totalDeliveries; }
 public void setTotalDeliveries(int totalDeliveries) { this.totalDeliveries = totalDeliveries; }
 public String getVehiclePlateNumber() { return vehiclePlateNumber; }
 public void setVehiclePlateNumber(String vehiclePlateNumber) { this.vehiclePlateNumber = vehiclePlateNumber; }

 public Driver(String username, String password, String phoneNum, String driverLicence, VehicleType vehicleType, LocalDate birthDate) {
 super(username, password, phoneNum);
 this.driverLicence = driverLicence;
 this.vehicleType = vehicleType;
 this.birthDate = birthDate;
 this.isAvailable = true;
 }

 public Driver(String username, String password, String phoneNum, String driverLicence, VehicleType vehicleType, LocalDate birthDate, String vehiclePlateNumber) {
 super(username, password, phoneNum);
 this.driverLicence = driverLicence;
 this.vehicleType = vehicleType;
 this.birthDate = birthDate;
 this.vehiclePlateNumber = vehiclePlateNumber;
 this.isAvailable = true;
 }
}
