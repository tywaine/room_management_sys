package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.Furniture;

public class FurnitureDTO {
    private Integer id;
    private Integer roomID;
    private String furnitureType;
    private String furnitureCondition;

    public FurnitureDTO() {}

    public FurnitureDTO(Integer roomID, String furnitureType, String furnitureCondition) {
        this.roomID = roomID;
        this.furnitureType = furnitureType;
        this.furnitureCondition = furnitureCondition;
    }

    public FurnitureDTO(Integer id, Integer roomID, String furnitureType, String furnitureCondition) {
        this.id = id;
        this.roomID = roomID;
        this.furnitureType = furnitureType;
        this.furnitureCondition = furnitureCondition;

        if(id != null) {
            new Furniture(id, roomID, furnitureType, furnitureCondition);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public String getFurnitureType() {
        return furnitureType;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }

    public String getFurnitureCondition() {
        return furnitureCondition;
    }

    public void setFurnitureCondition(String furnitureCondition) {
        this.furnitureCondition = furnitureCondition;
    }

    @Override
    public String toString() {
        return "FurnitureDTO{" +
                "id=" + id +
                ", roomID=" + roomID +
                ", furnitureType='" + furnitureType + '\'' +
                ", furnitureCondition='" + furnitureCondition + '\'' +
                '}';
    }
}
