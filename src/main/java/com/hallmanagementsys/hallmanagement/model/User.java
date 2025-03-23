package com.hallmanagementsys.hallmanagement.model;

import com.hallmanagementsys.hallmanagement.dto.UserDTO;
import com.hallmanagementsys.hallmanagement.util.DateTimeUtil;
import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty passwordHash = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final StringProperty createdAtFormatted = new SimpleStringProperty();

    public User(String username, String passwordHash, String role, LocalDateTime createdAt) {
        this.id.set(0);
        setUsername(username);
        setPasswordHash(passwordHash);
        setRole(role);
        setCreatedAt(createdAt);
    }

    public User(Integer id, String username, String passwordHash, String role, LocalDateTime createdAt) {
        this.id.set(id);
        setUsername(username);
        setPasswordHash(passwordHash);
        setRole(role);
        setCreatedAt(createdAt);

        this.createdAtFormatted.set(DateTimeUtil.formatPretty(createdAt));
    }

    // Properties
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordHashProperty() {
        return passwordHash;
    }

    public StringProperty roleProperty() {
        return role;
    }

    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    // Getters
    public Integer getID() {
        return id.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPasswordHash() {
        return passwordHash.get();
    }

    public String getRole() {
        return role.get();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public String getCreatedAtFormatted() {
        return createdAtFormatted.get();
    }

    public StringProperty createdAtFormattedProperty(){
        return createdAtFormatted;
    }

    // Setters
    public void setID(Integer id) {
        this.id.set(id);
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash.set(passwordHash);
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt.set(createdAt);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getID() +
                ", username='" + getUsername() + '\'' +
                ", passwordHash='" + getPasswordHash() + '\'' +
                ", role='" + getRole() + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj){
            return true;
        }

        if (obj == null || getClass() != obj.getClass()){
            return false;
        }

        User user = (User) obj;
        return Objects.equals(getID(), user.getID());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getID());
    }

    public boolean isStaff(){
        return getRole().equals("STAFF");
    }

    public boolean isAdmin(){
        return getRole().equals("ADMIN");
    }

    // Convert User -> UserDTO
    public UserDTO toDTO() {
        if(getID() == 0){
            return new UserDTO(null, getUsername(), getPasswordHash(), getRole(), getCreatedAt());
        }

        return new UserDTO(getID(), getUsername(), getPasswordHash(), getRole(), getCreatedAt());
    }

    // Convert UserDTO -> User
    public static User fromDTO(UserDTO dto) {
        return new User(dto.getId(), dto.getUsername(), dto.getPasswordHash(), dto.getRole(), dto.getCreatedAt());
    }

    public static boolean isValidUsername(String name) {
        // Check if the string contains a space
        if (name == null || name.isEmpty()) {
            return false;
        }

        if (name.contains(" ")) {
            return false;
        }

        if(Character.isDigit(name.charAt(0))){
            return false;
        }

        // Check if the string contains both '-' and '_'
        if (name.contains("-") && name.contains("_")) {
            return false;
        }

        int end = name.length() - 1;

        if(!Character.isAlphabetic(name.charAt(end)) && !Character.isDigit(name.charAt(end))){
            return false;
        }

        // Ensure '-' or '_' appears at most once
        long hyphenCount = name.chars().filter(ch -> ch == '-').count();
        long underscoreCount = name.chars().filter(ch -> ch == '_').count();

        if (hyphenCount > 1 || underscoreCount > 1) {
            return false;
        }

        // Ensure the string consists of only alphanumeric characters and the allowed special character
        return name.matches("^[A-Za-z0-9-_]+$");
    }

    public static boolean isValidPasswordLength(String password){
        return password.length() >= 8 && password.length() <= 50;
    }

    public static int usernameMaxLength(){
        return 50;
    }

    public void update(String username, String passwordHash, String role){
        if(username != null){
            setUsername(username);
        }

        if(passwordHash != null){
            setPasswordHash(passwordHash);
        }

        if(role != null){
            setRole(role);
        }
    }
}
