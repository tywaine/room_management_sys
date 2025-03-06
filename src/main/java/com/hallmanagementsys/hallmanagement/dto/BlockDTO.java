package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.Block;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BlockDTO {
    private Integer id;
    private Character name;
    private Integer maxRooms;

    public BlockDTO(Character name, Integer maxRooms) {
        this.name = name;
        this.maxRooms = maxRooms;
    }

    public BlockDTO(Integer id, Character name, Integer maxRooms) {
        this.id = id;
        this.name = name;
        this.maxRooms = maxRooms;

        if(id != null) {
            new Block(id, name, maxRooms);
        }
    }
}
