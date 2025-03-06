package com.hallmanagementsys.hallmanagement.dto.msg;

import com.hallmanagementsys.hallmanagement.dto.FurnitureDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FurnitureUpdateMessage {
    private FurnitureDTO furniture;
    private String status; // ADD, UPDATE

}
