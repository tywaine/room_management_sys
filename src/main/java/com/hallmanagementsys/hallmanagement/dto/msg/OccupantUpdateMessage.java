package com.hallmanagementsys.hallmanagement.dto.msg;

import com.hallmanagementsys.hallmanagement.dto.OccupantDTO;

public class OccupantUpdateMessage {
    private OccupantDTO occupant;
    private String status; // ADD, UPDATE

    public OccupantUpdateMessage() {
    }

    public OccupantUpdateMessage(OccupantDTO occupant, String status) {
        this.occupant = occupant;
        this.status = status;
    }

    public OccupantDTO getOccupant() {
        return occupant;
    }

    public void setOccupant(OccupantDTO occupant) {
        this.occupant = occupant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OccupantUpdateMessage{" +
                "occupant=" + occupant +
                ", status='" + status + '\'' +
                '}';
    }
}
