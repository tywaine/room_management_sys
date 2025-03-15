package com.hallmanagementsys.hallmanagement.model;

import com.hallmanagementsys.hallmanagement.dto.OccupantDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Occupant {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty idNumber = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phoneNumber = new SimpleStringProperty();
    private final IntegerProperty roomID = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> dateAdded = new SimpleObjectProperty<>();
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

    public static void update(Occupant occupant, String firstName, String lastName, String idNumber, String email, String phoneNumber, Integer roomID) {
        if(occupant != null){
            occupant.setFirstName(firstName);
            occupant.setLastName(lastName);
            occupant.setIdNumber(idNumber);
            occupant.setEmail(email);
            occupant.setPhoneNumber(phoneNumber);
            occupant.setRoomID(roomID);
        }
        else{
            System.out.println("Occupant value is null");
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
