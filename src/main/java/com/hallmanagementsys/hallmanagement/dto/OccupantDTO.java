package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.Occupant;

import java.time.LocalDateTime;

public class OccupantDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String email;
    private String phoneNumber;
    private Integer roomID;
    private LocalDateTime dateAdded;

    public OccupantDTO(){}

    public OccupantDTO(Integer id, String firstName, String lastName, String idNumber, String email, String phoneNumber, Integer roomID, LocalDateTime dateAdded) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roomID = roomID;
        this.dateAdded = dateAdded;

        if(id != null) {
            new Occupant(id, firstName, lastName, idNumber, email, phoneNumber, roomID, dateAdded);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "OccupantDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roomID=" + roomID +
                ", dateAdded=" + dateAdded +
                '}';
    }
}
