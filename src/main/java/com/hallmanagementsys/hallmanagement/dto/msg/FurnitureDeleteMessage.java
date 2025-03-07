package com.hallmanagementsys.hallmanagement.dto.msg;

public class FurnitureDeleteMessage {
    private Integer furnitureId;
    private String message;

    public FurnitureDeleteMessage() {
    }

    public FurnitureDeleteMessage(Integer furnitureId, String message) {
        this.furnitureId = furnitureId;
        this.message = message;
    }

    public Integer getFurnitureId() {
        return furnitureId;
    }

    public void setFurnitureId(Integer furnitureId) {
        this.furnitureId = furnitureId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FurnitureDeleteMessage{" +
                "furnitureId=" + furnitureId +
                ", message='" + message + '\'' +
                '}';
    }
}
