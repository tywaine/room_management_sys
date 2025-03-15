package com.hallmanagementsys.hallmanagement.dto.msg;

import com.hallmanagementsys.hallmanagement.dto.FurnitureDTO;

public class FurnitureUpdateMessage {
    private FurnitureDTO furniture;
    private String status; // ADD, UPDATE

    public FurnitureUpdateMessage() {
    }

    public FurnitureUpdateMessage(FurnitureDTO furniture, String status) {
        this.furniture = furniture;
        this.status = status;
    }

    public FurnitureDTO getFurniture() {
        return furniture;
    }

    public void setFurniture(FurnitureDTO furniture) {
        this.furniture = furniture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isStatusADD(){
        return status.equals("ADD");
    }

    public boolean isStatusUPDATE(){
        return status.equals("UPDATE");
    }

    @Override
    public String toString() {
        return "FurnitureUpdateMessage{" +
                "furniture=" + furniture +
                ",\nstatus='" + status + '\'' +
                '}';
    }
}
