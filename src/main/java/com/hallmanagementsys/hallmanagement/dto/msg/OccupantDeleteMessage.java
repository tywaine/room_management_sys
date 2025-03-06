package com.hallmanagementsys.hallmanagement.dto.msg;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OccupantDeleteMessage {
    private Integer occupantId;
    private String message;
}
