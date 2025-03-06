package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RoomDTO {
    private Integer id;
    private Integer blockID;
    private String roomNumber;
    private Integer floor;
    private Integer maxOccupants;

    public RoomDTO(Integer blockID, String roomNumber, Integer floor, Integer maxOccupants) {
        this.blockID = blockID;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.maxOccupants = maxOccupants;
    }

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
}
