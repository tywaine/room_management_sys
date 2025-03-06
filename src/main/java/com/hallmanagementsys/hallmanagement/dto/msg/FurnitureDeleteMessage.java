package com.hallmanagementsys.hallmanagement.dto.msg;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FurnitureDeleteMessage {
    private Integer furnitureId;
    private String message;
}
