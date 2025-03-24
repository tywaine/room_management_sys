package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.Room;

public class RoomDTO {
    private Integer id;
    private Integer blockID;
    private String roomNumber;
    private Integer maxOccupants;

    public RoomDTO(){}

    public RoomDTO(Integer id, Integer blockID, String roomNumber, Integer maxOccupants) {
        this.id = id;
        this.blockID = blockID;
        this.roomNumber = roomNumber;
        this.maxOccupants = maxOccupants;

        if(id != null) {
            new Room(id, blockID, roomNumber, maxOccupants);
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
                ", maxOccupants=" + maxOccupants +
                '}';
    }
}
