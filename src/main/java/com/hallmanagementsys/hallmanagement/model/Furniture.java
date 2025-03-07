package com.hallmanagementsys.hallmanagement.model;

import com.hallmanagementsys.hallmanagement.dto.FurnitureDTO;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Furniture {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty roomID = new SimpleIntegerProperty();
    private final StringProperty furnitureType = new SimpleStringProperty();
    private final StringProperty furnitureCondition = new SimpleStringProperty();
    private static final Map<Integer, Furniture> furniture = new HashMap<>();
    private static final ObservableList<Furniture> furnitureList = FXCollections.observableArrayList();

    public Furniture(Integer id, Integer roomID, String furnitureType, String furnitureCondition) {
        this.id.set(id);
        setRoomID(roomID);
        setFurnitureType(furnitureType);
        setFurnitureCondition(furnitureCondition);

        addFurniture(this);
    }


    // Properties
    public IntegerProperty idProperty() {
        return id;
    }

    public IntegerProperty roomIDProperty() {
        return roomID;
    }

    public StringProperty furnitureTypeProperty() {
        return furnitureType;
    }

    public StringProperty furnitureConditionProperty() {
        return furnitureCondition;
    }

    // Getters
    public Integer getID() {
        return id.get();
    }

    public Integer getRoomID() {
        return roomID.get();
    }

    public String getFurnitureType() {
        return furnitureType.get();
    }

    public String getFurnitureCondition() {
        return furnitureCondition.get();
    }

    // Setters
    public void setID(Integer id) {
        this.id.set(id);
    }

    public void setRoomID(Integer roomID) {
        this.roomID.set(roomID);
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType.set(furnitureType);
    }

    public void setFurnitureCondition(String furnitureCondition) {
        this.furnitureCondition.set(furnitureCondition);
    }

    @Override
    public String toString() {
        return "Furniture{" +
                "id=" + getID() +
                ", roomID=" + getRoomID() +
                ", furnitureType=" + getFurnitureType() +
                ", furnitureCondition=" + getFurnitureCondition() +
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

        Furniture furniture = (Furniture) obj;
        return Objects.equals(getID(), furniture.getID());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getID());
    }

    // Convert Furniture -> FurnitureDTO
    public FurnitureDTO toDTO() {
        if(getID() == 0) {
            return new FurnitureDTO(null, getRoomID(), getFurnitureType(), getFurnitureCondition());
        }

        return new FurnitureDTO(getID(), getRoomID(), getFurnitureType(), getFurnitureCondition());
    }

    // Convert FurnitureDTO -> Furniture
    public static Furniture fromDTO(FurnitureDTO dto) {
        return new Furniture(dto.getId(), dto.getRoomID(), dto.getFurnitureType(), dto.getFurnitureCondition());
    }

    public static void addFurniture(Furniture furniture) {
        if(furniture == null){
            System.out.println("Furniture is null. Was not added to Map and List");
        }
        else{
            if(!containsFurniture(furniture.getID())){
                Furniture.furniture.put(furniture.getID(), furniture);
                furnitureList.add(furniture);
                Room room = Room.getRoom(furniture.getRoomID());

                if(Room.isValidRoom(room)){
                    room.addFurniture(furniture);
                }
            }
            else{
                System.out.println("Furniture is already present!");
            }
        }
    }

    public static void update(Furniture furniture, Integer roomID, String furnitureType, String furnitureCondition) {
        if(furniture != null){
            furniture.setRoomID(roomID);
            furniture.setFurnitureType(furnitureType);
            furniture.setFurnitureCondition(furnitureCondition);
        }
        else{
            System.out.println("Furniture value is null");
        }
    }

    public static void removeFurniture(Furniture furniture) {
        if(isValidFurniture(furniture)){
            furnitureList.remove(furniture);
            Furniture.furniture.remove(furniture.getID());
            Room room = Room.getRoom(furniture.getRoomID());

            if(Room.isValidRoom(room)){
                room.removeFurniture(furniture);
            }
        }
        else{
            System.out.println("Furniture ID not found");
        }
    }

    public static Furniture getFurniture(Integer furnitureID) {
        if(containsFurniture(furnitureID)){
            return furniture.get(furnitureID);
        }
        else{
            System.out.println("Furniture ID not found. Null was returned");
            return null;
        }
    }

    public static boolean isValidFurniture(Furniture furniture) {
        return furniture != null && containsFurniture(furniture.getID());
    }

    public static boolean containsFurniture(Integer furnitureID) {
        return furniture.containsKey(furnitureID);
    }

    public static ObservableList<Furniture> getList(){
        return furnitureList;
    }

    public static void emptyFurniture() {
        furniture.clear();
        furnitureList.clear();
    }
}
