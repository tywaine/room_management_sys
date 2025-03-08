package com.hallmanagementsys.hallmanagement.dto;

import com.hallmanagementsys.hallmanagement.model.Block;

public class BlockDTO {
    private Integer id;
    private Character name;
    private Integer maxRooms;

    public BlockDTO() {}

    public BlockDTO(Integer id, Character name, Integer maxRooms) {
        this.id = id;
        this.name = name;
        this.maxRooms = maxRooms;

        if(id != null) {
            new Block(id, name, maxRooms);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Character getName() {
        return name;
    }

    public void setName(Character name) {
        this.name = name;
    }

    public Integer getMaxRooms() {
        return maxRooms;
    }

    public void setMaxRooms(Integer maxRooms) {
        this.maxRooms = maxRooms;
    }

    @Override
    public String toString() {
        return "BlockDTO{" +
                "id=" + id +
                ", name=" + name +
                ", maxRooms=" + maxRooms +
                '}';
    }
}
