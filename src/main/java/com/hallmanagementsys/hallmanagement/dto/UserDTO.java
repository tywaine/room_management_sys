package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    private Integer id;
    private String username;
    private String passwordHash;
    private String role;
    private LocalDateTime createdAt;

    // Constructor for easy conversion
    public UserDTO(Integer id, String username, String passwordHash, String role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;

        if(id != null) {
            new User(id, username, passwordHash, role, createdAt);
        }
    }
}

