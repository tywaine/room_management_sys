package com.hallmanagementsys.hallmanagement.model;

import com.hallmanagementsys.hallmanagement.dto.UserDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty passwordHash = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final StringProperty createdAtFormatted = new SimpleStringProperty();

    private static final Map<Integer, User> staffs = new HashMap<>();
    private static final ObservableList<User> staffList = FXCollections.observableArrayList();

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.createdAtFormatted.set(createdAt.format(formatter));

        addStaff(this);
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

    public static void update(User user, String username, String passwordHash, String role){
        if(user != null){
            user.setUsername(username);
            user.setPasswordHash(passwordHash);
            user.setRole(role);
        }
        else{
            System.out.println("User value is null");
        }
    }

    public static void addStaff(User staff) {
        if(staff == null){
            System.out.println("User is null. Was not added to Map and List");
            return;
        }
        if(staff.isStaff()){
            if(!containsStaff(staff.getID())){
                staffs.put(staff.getID(), staff);
                staffList.add(staff);
                System.out.println("User's role is STAFF");
            }
            else{
                System.out.println("Staff is already present!");
            }
        }
        else{
            System.out.println("User's role is ADMIN");
        }
    }

    public static void removeStaff(User staff) {
        if(isValidStaff(staff)){
            staffList.remove(staff);
            staffs.remove(staff.getID());
        }
        else{
            System.out.println("Staff ID not found");
        }
    }

    public static User getStaff(Integer staffId) {
        if(containsStaff(staffId)){
            return staffs.get(staffId);
        }
        else{
            System.out.println("Staff ID not found. Null was returned");
            return null;
        }
    }

    public static boolean isValidStaff(User staff){
        return staff != null && containsStaff(staff.getID());
    }

    public static boolean containsStaff(Integer staffID) {
        return staffs.containsKey(staffID);
    }

    public static ObservableList<User> getStaffList(){
        return staffList;
    }

    public static void emptyStaff() {
        staffs.clear();
        staffList.clear();
    }
}
