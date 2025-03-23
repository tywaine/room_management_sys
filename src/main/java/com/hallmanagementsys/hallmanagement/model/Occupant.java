package com.hallmanagementsys.hallmanagement.model;

import com.hallmanagementsys.hallmanagement.dto.OccupantDTO;
import com.hallmanagementsys.hallmanagement.util.DateTimeUtil;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.*;

public class Occupant {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty idNumber = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final IntegerProperty roomID = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> dateAdded = new SimpleObjectProperty<>();
    private final StringProperty dateAddedFormatted = new SimpleStringProperty();
    private static final Map<Integer, Occupant> occupants = new HashMap<>();
    private static final ObservableList<Occupant> occupantList = FXCollections.observableArrayList();

    public Occupant(String firstName, String lastName, String idNumber, String email, String phoneNumber, Integer roomID, LocalDateTime dateAdded) {
        this.id.set(0);
        setFirstName(firstName);
        setLastName(lastName);
        setIdNumber(idNumber);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setRoomID(roomID);
        setDateAdded(dateAdded);
    }

    public Occupant(Integer id, String firstName, String lastName, String idNumber, String email, String phoneNumber, Integer roomID, LocalDateTime dateAdded) {
        this.id.set(id);
        setFirstName(firstName);
        setLastName(lastName);
        setIdNumber(idNumber);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setRoomID(roomID);
        setDateAdded(dateAdded);

        this.dateAddedFormatted.set(DateTimeUtil.formatPretty(dateAdded));

        addOccupant(this);
    }

    // Properties
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty idNumberProperty() {
        return idNumber;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public IntegerProperty roomIDProperty() {
        return roomID;
    }

    public ObjectProperty<LocalDateTime> dateAddedProperty() {
        return dateAdded;
    }

    public StringProperty dateAddedFormattedProperty() {
        return dateAddedFormatted;
    }

    // Getters
    public Integer getID() {
        return id.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getIdNumber() {
        return idNumber.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public Integer getRoomID() {
        return roomID.get();
    }

    public LocalDateTime getDateAdded() {
        return dateAdded.get();
    }

    // Setters
    public void setID(Integer id) {
        this.id.set(id);
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public void setIdNumber(String idNumber) {
        this.idNumber.set(idNumber);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public void setRoomID(Integer roomID) {
        this.roomID.set(roomID);
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded.set(dateAdded);
    }

    @Override
    public String toString() {
        return "Occupant{" +
                "id=" + getID() +
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", idNumber=" + getIdNumber() +
                ", email=" + getEmail() +
                ", phoneNumber=" + getPhoneNumber() +
                ", roomID=" + getRoomID() +
                ", dateAdded=" + getDateAdded() +
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

        Occupant occupant = (Occupant) obj;
        return Objects.equals(getID(), occupant.getID());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getID());
    }

    public void update(String firstName, String lastName, String idNumber, String email, String phoneNumber, Integer roomID) {
        if(firstName != null){
            setFirstName(firstName);
        }

        if(lastName != null){
            setLastName(lastName);
        }

        if(idNumber != null){
            setIdNumber(idNumber);
        }

        if(email != null){
            setEmail(email);
        }

        if(phoneNumber != null){
            setPhoneNumber(phoneNumber);
        }

        if(roomID != null){
            setRoomID(roomID);
        }
    }

    // Convert Occupant -> OccupantDTO
    public OccupantDTO toDTO() {
        if(getID() == 0) {
            return new OccupantDTO(null, getFirstName(), getLastName(), getIdNumber(), getEmail(), getPhoneNumber(), getRoomID(), getDateAdded());
        }

        return new OccupantDTO(getID(), getFirstName(), getLastName(), getIdNumber(), getEmail(), getPhoneNumber(), getRoomID(), getDateAdded());
    }

    // Convert FurnitureDTO -> Furniture
    public static Occupant fromDTO(OccupantDTO dto) {
        return new Occupant(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getIdNumber(), dto.getEmail(), dto.getPhoneNumber(), dto.getRoomID(), dto.getDateAdded());
    }

    public static boolean isValidName(String name){
        if(name == null || name.isEmpty()){
            return false;
        }

        if(name.length() > 50){
            return false;
        }

        return name.matches("^[A-Za-z]+$");
    }

    public static boolean isValidEmail(String email){
        if(email == null || email.isEmpty()){
            return false;
        }

        if(email.length() > 100){
            return false;
        }

        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPhoneNumber(String phoneNumber){
        if(phoneNumber == null || phoneNumber.isEmpty()){
            return false;
        }

        return phoneNumber.matches("\\d{3}-\\d{4}");
    }

    public static boolean isValidIdNumber(String idNumber){
        if(idNumber == null || idNumber.isEmpty()){
            return false;
        }

        return  idNumber.matches("\\d{9}");
    }

    public static boolean idNumberExist(String idNumber){
        if(idNumber == null || idNumber.isEmpty()){
            return false;
        }

        for(Occupant occupant: getList()){
            if(occupant.getIdNumber().equals(idNumber)){
                return true;
            }
        }

        return false;
    }

    public static boolean emailExist(String email){
        if(email == null || email.isEmpty()){
            return false;
        }

        for(Occupant occupant: getList()){
            if(occupant.getEmail().equals(email)){
                return true;
            }
        }

        return false;
    }

    public static boolean phoneNumberExist(String phoneNumber){
        if(phoneNumber == null || phoneNumber.isEmpty()){
            return false;
        }

        for(Occupant occupant: getList()){
            if(occupant.getPhoneNumber().equals(phoneNumber)){
                return true;
            }
        }

        return false;
    }

    public static void addOccupant(Occupant occupant) {
        if(occupant == null){
            System.out.println("Occupant is null. Was not added to Map and List");
        }
        else{
            if(!containsOccupant(occupant.getID())){
                occupants.put(occupant.getID(), occupant);
                occupantList.add(occupant);

                Room room = Room.getRoom(occupant.getRoomID());

                if(Room.isValidRoom(room)){
                    room.addOccupant(occupant);
                }
            }
            else{
                System.out.println("Occupant is already present!");
            }
        }
    }

    public static void removeOccupant(Occupant occupant) {
        if(isValidOccupant(occupant)){
            occupantList.remove(occupant);
            occupants.remove(occupant.getID());

            Room room = Room.getRoom(occupant.getRoomID());

            if(Room.isValidRoom(room)){
                room.removeOccupant(occupant);
            }
        }
        else{
            System.out.println("Occupant ID not found");
        }
    }

    public static Occupant getOccupant(Integer occupantID) {
        if(containsOccupant(occupantID)){
            return occupants.get(occupantID);
        }
        else{
            System.out.println("Occupant ID not found. Null was returned");
            return null;
        }
    }

    public static Occupant getOccupantByIdNumber(String idNumber){
        if(idNumber == null || idNumber.isEmpty()){
            return null;
        }
        else{
            for(Occupant occupant: occupantList){
                if(occupant.getIdNumber().equals(idNumber)){
                    return occupant;
                }
            }
        }

        return null;
    }

    public static Occupant getOccupantByEmail(String email) {
        if(email == null || email.isEmpty()){
            return null;
        }
        else{
            for(Occupant occupant: occupantList){
                if(occupant.getEmail().equals(email)){
                    return occupant;
                }
            }
        }

        return null;
    }

    public static Occupant getOccupantByPhoneNumber(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isEmpty()){
            return null;
        }
        else{
            for(Occupant occupant: occupantList){
                if(occupant.getPhoneNumber().equals(phoneNumber)){
                    return occupant;
                }
            }
        }

        return null;
    }

    public static boolean isValidOccupant(Occupant occupant) {
        return occupant != null && containsOccupant(occupant.getID());
    }

    public static boolean containsOccupant(Integer occupantID) {
        return occupants.containsKey(occupantID);
    }

    public static ObservableList<Occupant> getList(){
        return occupantList;
    }

    public static void emptyOccupants() {
        occupants.clear();
        occupantList.clear();
    }
}
