package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.Room;

public class RoomDTO {
    private Integer id;
    private Integer blockID;
    private String roomNumber;
    private Integer floor;
    private Integer maxOccupants;

    public RoomDTO(){}

    public RoomDTO(Integer id, Integer blockID, String roomNumber, Integer floor, Integer maxOccupants) {
        this.id = id;
        this.blockID = blockID;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.maxOccupants = maxOccupants;

        if(id != null) {
            new Room(id, blockID, roomNumber, floor, maxOccupants);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlockID() {
        return blockID;
    }

    public void setBlockID(Integer blockID) {
        this.blockID = blockID;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getMaxOccupants() {
        return maxOccupants;
    }

    public void setMaxOccupants(Integer maxOccupants) {
        this.maxOccupants = maxOccupants;
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "id=" + id +
                ", blockID=" + blockID +
                ", roomNumber='" + roomNumber + '\'' +
                ", floor=" + floor +
                ", maxOccupants=" + maxOccupants +
                '}';
    }
}
