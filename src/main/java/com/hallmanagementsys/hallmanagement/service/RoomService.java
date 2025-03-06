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

    private void getRooms(String url){
        try{
            HttpResponse<String> response = HttpRequestUtil.sendRequest("GET", url);

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

    public void fetchRooms() {
        getRooms(baseUrl);
    }

    public void fetchRooms(Integer id) {
        // Build the URL based on the presence of ID
        String url = (id != null) ? baseUrl + "?id=" + id : baseUrl;
        getRooms(url);
    }

    public HttpResponse<String> sendCreateRoomRequest(RoomDTO roomDTO) {
        try {
            // Convert room object to JSON
            String roomJson = Json.stringify(Json.toJson(roomDTO));
            System.out.println(roomJson);

            return HttpRequestUtil.sendRequest(
                    "POST",
                    baseUrl,
                    Map.of("Content-Type", "application/json"),
                    roomJson
            );
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error",
                    "An error occurred while sending the create room post request: " + e.getMessage());
            return null; // Return null in case of an error
        }
    }

    public Room createRoomAndRetrieve(RoomDTO roomDTO) {
        HttpResponse<String> response = sendCreateRoomRequest(roomDTO);

        if (response == null) {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
            return null;
        }

        if (response.statusCode() == HttpStatus.CREATED) {  // Check if response is CREATED
            try {
                RoomDTO room = Json.fromJson(response.body(), RoomDTO.class);
                return Room.fromDTO(room);
            } catch (Exception e) {
                e.printStackTrace();
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse room from response.");
            }
        } else {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to create room. Server responded with: " + response.body());
        }

        return null;
    }

    public boolean updateRoom(Integer roomID, Integer blockID, String roomNumber, Integer floor, Integer maxOccupants) {
        try {
            String updateUrl = baseUrl + "/update/" + roomID;

            // Build the request body only with non-null parameters
            List<String> params = new ArrayList<>();

            if (blockID != null){
                params.add("blockID=" + blockID);
            }

            if (roomNumber != null){
                params.add("passwordHash=" + URLEncoder.encode(roomNumber, StandardCharsets.UTF_8));
            }

            if (floor != null){
                params.add("floor=" + floor);
            }

            if (maxOccupants != null){
                params.add("maxOccupants=" + maxOccupants);
            }

            // Append parameters to URL if there are any
            if (!params.isEmpty()) {
                updateUrl += "?" + String.join("&", params);
            }

            // Send the PUT request
            HttpResponse<String> response = HttpRequestUtil.sendRequest(
                    "PUT",
                    updateUrl,
                    Map.of("Content-Type", "application/x-www-form-urlencoded"),
                    null
            ); // No request body needed for URL-encoded parameters in a PUT request

            return response.statusCode() == HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the room: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteRoom(Integer roomID) {
        try {
            String deleteUrl = baseUrl + "/delete?id=" + roomID;

            // Send the DELETE request
            HttpResponse<String> response =  HttpRequestUtil.sendRequest("DELETE", deleteUrl);

            return response.statusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the room: " + e.getMessage());
            return false;
        }
    }

}
