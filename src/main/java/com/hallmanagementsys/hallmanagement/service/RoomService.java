package com.hallmanagementsys.hallmanagement.service;

import com.hallmanagementsys.hallmanagement.dto.RoomDTO;
import com.hallmanagementsys.hallmanagement.model.Room;
import com.hallmanagementsys.hallmanagement.util.*;
import javafx.scene.control.Alert;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomService {
    private static final String baseUrl = MyUrl.getUrl() + "rooms";

    private static RoomService instance;

    private RoomService() {}

    public static synchronized RoomService getInstance() {
        if (instance == null) {
            instance = new RoomService();
        }

        return instance;
    }

    public void fetchRooms() {
        try{
            HttpResponse<String> response = HttpRequestUtil.sendRequest("GET", baseUrl);

            // Check the response status code
            if (response.statusCode() == HttpStatus.OK) {
                // Convert JSON response to List<Room>
                List<RoomDTO> roomDTOS = Json.fromJsonArray(response.body(), RoomDTO.class);

                if (roomDTOS != null) {

                    // Convert DTOs to Room objects
                    for (RoomDTO roomDTO : roomDTOS) {
                        Room.fromDTO(roomDTO);
                        //System.out.println(roomDTO);
                    }

                    System.out.println("Fetched " + roomDTOS.size() + " rooms.");
                }
                else {
                    MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse rooms.");
                }
            }
            else {
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch rooms: " + response.body());
            }
        }catch(Exception e){
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching rooms: " + e.getMessage());
        }
    }

}
