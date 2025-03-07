package com.hallmanagementsys.hallmanagement.model;

import com.hallmanagementsys.hallmanagement.dto.RoomDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Room {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty blockID = new SimpleIntegerProperty(); // Using Character
    private final StringProperty roomNumber = new SimpleStringProperty();
    private final IntegerProperty floor = new SimpleIntegerProperty();
    private final IntegerProperty maxOccupants = new SimpleIntegerProperty();
    private final IntegerProperty currentOccupants = new SimpleIntegerProperty();
    private final IntegerProperty totalFurniture = new SimpleIntegerProperty();
    private final ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();
    private final ObservableList<Occupant> occupantList = FXCollections.observableArrayList();

    private static final Map<Integer, Room> rooms = new HashMap<>();
    private static final ObservableList<Room> roomList = FXCollections.observableArrayList();

    public Room(Integer blockID, String roomNumber, Integer floor, Integer maxOccupants) {
        this.blockID.set(blockID);
        this.roomNumber.set(roomNumber);
        this.floor.set(floor);
        this.maxOccupants.set(maxOccupants);
    }

    public Room(Integer id, Integer blockID, String roomNumber, Integer floor, Integer maxOccupants) {
        this.id.set(id);
        this.blockID.set(blockID);
        this.roomNumber.set(roomNumber);
        this.floor.set(floor);
        this.maxOccupants.set(maxOccupants);

        addRoom(this);
    }

    // Getters
    public Integer getID() {
        return id.get();
    }

    public Integer getBlockID() {
        return blockID.get();
    }

    public String getRoomNumber() {
        return roomNumber.get();
    }

    public Integer getFloor() {
        return floor.get();
    }

    public Integer getMaxOccupants() {
        return maxOccupants.get();
    }

    public Integer getCurrentOccupants() {
        return currentOccupants.get();
    }

    public Integer getTotalFurniture() {
        return totalFurniture.get();
    }

    // Setters
    public void setID(Integer id) {
        this.id.set(id);
    }

    public void setBlockID(Integer blockID) {
        this.blockID.set(blockID);
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public void setFloor(Integer floor) {
        this.floor.set(floor);
    }

    public void setMaxOccupants(int maxOccupants) {
        this.maxOccupants.set(maxOccupants);
    }

    public void setCurrentOccupants(int currentOccupants) {
        this.currentOccupants.set(currentOccupants);
    }

    public void setTotalFurniture(int totalFurniture) {
        this.totalFurniture.set(totalFurniture);
    }

    // Property Methods
    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty blockIDProperty() {
        return blockID;
    }

    public StringProperty roomNumberProperty() {
        return roomNumber;
    }

    public IntegerProperty floorProperty() {
        return floor;
    }

    public IntegerProperty maxOccupantsProperty() {
        return maxOccupants;
    }

    public IntegerProperty currentOccupantsProperty() {
        return currentOccupants;
    }

    public IntegerProperty totalFurnitureProperty() {
        return totalFurniture;
    }

    // Custom Methods
    public ObjectProperty<Character> getBlockNameProperty(){
        return Block.getBlock(getBlockID()).nameProperty();
    }

    public void addFurniture(Furniture furniture) {
        furnitureList.add(furniture);
        setTotalFurniture(furnitureList.size());
    }

    public void removeFurniture(Furniture furniture) {
        furnitureList.remove(furniture);
        setTotalFurniture(furnitureList.size());
    }

    public void addOccupant(Occupant occupant) {
        occupantList.add(occupant);
        setCurrentOccupants(occupantList.size());
    }

    public void removeOccupant(Occupant occupant) {
        occupantList.remove(occupant);
        setCurrentOccupants(occupantList.size());
    }

    public ObservableList<Furniture> getFurnitureList() {
        return furnitureList;
    }

    public ObservableList<Occupant> getOccupantList() {
        return occupantList;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + getID() +
                ", blockID=" + getBlockID() +
                ", roomNumber=" + getRoomNumber() +
                ", floor=" + getFloor() +
                ", maxOccupants=" + getMaxOccupants() +
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

        Room room = (Room) obj;
        return Objects.equals(getID(), room.getID());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getID());
    }

    // Convert Room -> RoomDTO
    public RoomDTO toDTO() {
        if(getID() == 0) {
            return new RoomDTO(null, getBlockID(), getRoomNumber(), getFloor(), getMaxOccupants());
        }

        return new RoomDTO(getID(), getBlockID(), getRoomNumber(), getFloor(), getMaxOccupants());
    }

    // Convert RoomDTO -> Room
    public static Room fromDTO(RoomDTO dto) {
        return new Room(dto.getId(), dto.getBlockID(), dto.getRoomNumber(), dto.getFloor(), dto.getMaxOccupants());
    }

    public static void addRoom(Room room) {
        if(room == null){
            System.out.println("Room is null. Was not added to Map and List");
        }
        else{
            if(!containsRoom(room.getID())){
                rooms.put(room.getID(), room);
                roomList.add(room);
            }
            else{
                System.out.println("Room is already present!");
            }
        }
    }

    public static void update(Room room, Integer blockID, String roomNumber, Integer floor, Integer maxOccupants) {
        if(room != null){
            room.setBlockID(blockID);
            room.setRoomNumber(roomNumber);
            room.setFloor(floor);
            room.setMaxOccupants(maxOccupants);
        }
        else{
            System.out.println("Room value is null");
        }
    }

    public static void removeRoom(Room room) {
        if(isValidRoom(room)){
            roomList.remove(room);
            rooms.remove(room.getID());
        }
        else{
            System.out.println("Room ID not found");
        }
    }

    public static Room getRoom(Integer roomID) {
        if(containsRoom(roomID)){
            return rooms.get(roomID);
        }
        else{
            System.out.println("Room ID not found. Null was returned");
            return null;
        }
    }

    public static boolean isValidRoom(Room room) {
        return room != null && containsRoom(room.getID());
    }

    public static boolean containsRoom(Integer roomID) {
        return rooms.containsKey(roomID);
    }

    public static ObservableList<Room> getList(){
        return roomList;
    }

    public static void emptyRooms() {
        rooms.clear();
        roomList.clear();
    }
}
