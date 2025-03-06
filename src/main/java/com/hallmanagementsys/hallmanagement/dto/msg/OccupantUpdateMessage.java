package com.hallmanagementsys.hallmanagement.dto.msg;

import com.hallmanagementsys.hallmanagement.dto.OccupantDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OccupantUpdateMessage {
    private OccupantDTO occupant;
    private String status; // ADD, UPDATE
}
