package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.Occupant;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OccupantDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String email;
    private String phoneNumber;
    private Integer roomID;
    private LocalDateTime dateAdded;

    public OccupantDTO(String firstName, String lastName, String idNumber, String email, String phoneNumber, Integer roomID, LocalDateTime dateAdded) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roomID = roomID;
        this.dateAdded = dateAdded;
    }

    public OccupantDTO(String firstName, String lastName, String idNumber, String email, String phoneNumber, Integer roomID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roomID = roomID;
    }

    public OccupantDTO(Integer id, String firstName, String lastName, String idNumber, String email, String phoneNumber, Integer roomID) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.roomID = roomID;
    }

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
}
