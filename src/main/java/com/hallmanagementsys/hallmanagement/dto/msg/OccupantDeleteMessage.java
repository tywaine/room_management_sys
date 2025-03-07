package com.hallmanagementsys.hallmanagement.dto.msg;

public class OccupantDeleteMessage {
    private Integer occupantId;
    private String message;

    public OccupantDeleteMessage() {
    }

    public OccupantDeleteMessage(Integer occupantId, String message) {
        this.occupantId = occupantId;
        this.message = message;
    }

    public Integer getOccupantId() {
        return occupantId;
    }

    public void setOccupantId(Integer occupantId) {
        this.occupantId = occupantId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OccupantDeleteMessage{" +
                "occupantId=" + occupantId +
                ", message='" + message + '\'' +
                '}';
    }
}
