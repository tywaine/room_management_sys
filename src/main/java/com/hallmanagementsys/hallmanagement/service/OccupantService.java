package com.hallmanagementsys.hallmanagement.service;

import com.hallmanagementsys.hallmanagement.dto.OccupantDTO;
import com.hallmanagementsys.hallmanagement.model.Occupant;
import com.hallmanagementsys.hallmanagement.util.*;
import javafx.scene.control.Alert;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OccupantService {
    private static final String baseUrl = MyUrl.getUrl() + "occupants";

    private static OccupantService instance;

    private OccupantService() {}

    public static synchronized OccupantService getInstance() {
        if (instance == null) {
            instance = new OccupantService();
        }

        return instance;
    }

    private void getOccupants(String url) {
        try {
            // Send the request
            HttpResponse<String> response = HttpRequestUtil.sendRequest("GET", url);

            // Check the response status code
            if (response.statusCode() == HttpStatus.OK) {
                // Convert JSON response to List<Occupant>
                List<OccupantDTO> occupantDTOS = Json.fromJsonArray(response.body(), OccupantDTO.class);

                if (occupantDTOS != null) {

                    // Convert DTOs to Occupant objects
                    for (OccupantDTO occupantDTO : occupantDTOS) {
                        Occupant.fromDTO(occupantDTO);
                        //System.out.println(Json.prettyPrint(occupantDTO));
                    }

                    System.out.println("Fetched " + occupantDTOS.size() + " occupants.");
                } else {
                    MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse occupants.");
                }
            }
            else {
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch occupants: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching occupants: " + e.getMessage());
        }
    }

    public void fetchOccupants() {
        getOccupants(baseUrl);
    }

    public void fetchOccupants(Integer id) {
        // Build the URL based on the presence of ID
        String url = (id != null) ? baseUrl + "?id=" + id : baseUrl;
        getOccupants(url);
    }

    public HttpResponse<String> sendCreateOccupantRequest(Occupant occupant) {
        try {
            // Convert occupant object to JSON
            OccupantDTO occupantDTO = occupant.toDTO();
            String occupantJson = Json.stringify(Json.toJson(occupantDTO));
            System.out.println(occupantJson);

            // Expecting CREATED RESPONSE
            return HttpRequestUtil.sendRequest(
                    "POST",
                    baseUrl,
                    Map.of("Content-Type", "application/json"),
                    occupantJson
            );
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error",
                    "An error occurred while sending the create occupants post request: " + e.getMessage());
            return null; // Return null in case of an error
        }
    }

    public Occupant createOccupant(Occupant occupant) {
        HttpResponse<String> response = sendCreateOccupantRequest(occupant);

        if (response == null) {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
            return null;
        }

        if (response.statusCode() == HttpStatus.CREATED) {  // Check if response is CREATED
            try {
                OccupantDTO occupantDTO = Json.fromJson(response.body(), OccupantDTO.class);
                return Occupant.fromDTO(occupantDTO);
            } catch (Exception e) {
                e.printStackTrace();
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse occupant from response.");
            }
        } else {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to create occupant. Server responded with: " + response.body());
        }

        return null;
    }

    public boolean updateOccupant(Occupant occupant) {
        try {
            Integer occupantID = occupant.getID();
            String firstName = occupant.getFirstName();
            String lastName = occupant.getLastName();
            String idNumber = occupant.getIdNumber();
            String phoneNumber = occupant.getPhoneNumber();
            String email = occupant.getEmail();
            Integer roomID = occupant.getRoomID();

            String updateUrl = baseUrl + "/update/" + occupantID;

            // Build the request body only with non-null parameters
            List<String> params = new ArrayList<>();

            if (firstName != null){
                params.add("firstName=" + URLEncoder.encode(firstName, StandardCharsets.UTF_8));
            }

            if (lastName != null){
                params.add("lastName=" + URLEncoder.encode(lastName, StandardCharsets.UTF_8));
            }

            if (idNumber != null){
                params.add("idNumber=" + URLEncoder.encode(idNumber, StandardCharsets.UTF_8));
            }

            if (email != null){
                params.add("email=" + URLEncoder.encode(email, StandardCharsets.UTF_8));
            }

            if (phoneNumber != null){
                params.add("phoneNumber=" + URLEncoder.encode(phoneNumber, StandardCharsets.UTF_8));
            }

            if (roomID != null){
                params.add("roomID=" + roomID);
            }

            // Append parameters to URL if there are any
            if (!params.isEmpty()) {
                updateUrl += "?" + String.join("&", params);
            }

            // Send the PUT request and Expect OK Response
            HttpResponse<String> response = HttpRequestUtil.sendRequest(
                    "PUT",
                    updateUrl,
                    Map.of("Content-Type", "application/x-www-form-urlencoded"),
                    null
            ); // No request body needed for URL-encoded parameters in a PUT request

            return response.statusCode() == HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the occupant: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteOccupant(Integer occupantID) {
        try {
            String deleteUrl = baseUrl + "/delete?id=" + occupantID;

            // Send the DELETE request
            HttpResponse<String> response =  HttpRequestUtil.sendRequest("DELETE", deleteUrl);

            return response.statusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the occupant: " + e.getMessage());
            return false;
        }
    }
}
