package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.Furniture;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FurnitureDTO {
    private Integer id;
    private Integer roomID;
    private String furnitureType;
    private String furnitureCondition;

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
}
