package com.hallmanagementsys.hallmanagement.service;

import com.hallmanagementsys.hallmanagement.dto.FurnitureDTO;
import com.hallmanagementsys.hallmanagement.model.Furniture;
import com.hallmanagementsys.hallmanagement.util.*;
import javafx.scene.control.Alert;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FurnitureService {
    private static final String baseUrl = MyUrl.getUrl() + "furniture";

    private static FurnitureService instance;

    private FurnitureService() {}

    public static synchronized FurnitureService getInstance() {
        if (instance == null) {
            instance = new FurnitureService();
        }

        return instance;
    }

    private void getFurniture(String url) {
        try {
            // Send the request
            HttpResponse<String> response = HttpRequestUtil.sendRequest("GET", url);

            // Check the response status code
            if (response.statusCode() == HttpStatus.OK) {
                // Convert JSON response to List<Furniture>
                List<FurnitureDTO> furnitureDTOS = Json.fromJsonArray(response.body(), FurnitureDTO.class);

                if (furnitureDTOS != null) {

                    // Convert DTOs to Furniture objects
                    for (FurnitureDTO furnitureDTO : furnitureDTOS) {
                        Furniture.fromDTO(furnitureDTO);
                        //System.out.println(Json.prettyPrint(furnitureDTO));
                    }

                    System.out.println("Fetched " + furnitureDTOS.size() + " furniture.");
                } else {
                    MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse furniture.");
                }
            }
            else {
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch furniture: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching furniture: " + e.getMessage());
        }
    }

    public void fetchFurniture() {
        getFurniture(baseUrl);
    }

    public void fetchFurniture(Integer id) {
        // Build the URL based on the presence of ID
        String url = (id != null) ? baseUrl + "?id=" + id : baseUrl;
        getFurniture(url);
    }

    public HttpResponse<String> sendCreateFurnitureRequest(FurnitureDTO furnitureDTO) {
        try {
            // Convert furniture object to JSON
            String furnitureJson = Json.stringify(Json.toJson(furnitureDTO));
            System.out.println(furnitureJson);

            // Expecting CREATED RESPONSE
            return HttpRequestUtil.sendRequest(
                    "POST",
                    baseUrl,
                    Map.of("Content-Type", "application/json"),
                    furnitureJson
            );
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error",
                    "An error occurred while sending the create furniture post request: " + e.getMessage());
            return null; // Return null in case of an error
        }
    }

    public Furniture createFurnitureAndRetrieve(FurnitureDTO furnitureDTO) {
        HttpResponse<String> response = sendCreateFurnitureRequest(furnitureDTO);

        if (response == null) {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
            return null;
        }

        if (response.statusCode() == HttpStatus.CREATED) {  // Check if response is CREATED
            try {
                FurnitureDTO furniture = Json.fromJson(response.body(), FurnitureDTO.class);
                return Furniture.fromDTO(furniture);
            } catch (Exception e) {
                e.printStackTrace();
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse furniture from response.");
            }
        } else {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to create furniture. Server responded with: " + response.body());
        }

        return null;
    }

    public boolean updateFurniture(Integer furnitureID, Integer roomID, String furnitureType, String furnitureCondition) {
        try {
            String updateUrl = baseUrl + "/update/" + furnitureID;

            // Build the request body only with non-null parameters
            List<String> params = new ArrayList<>();

            if (roomID != null){
                params.add("roomID=" + roomID);
            }

            if (furnitureType != null){
                params.add("furnitureType=" + URLEncoder.encode(furnitureType, StandardCharsets.UTF_8));
            }

            if (furnitureCondition != null){
                params.add("furnitureCondition=" + URLEncoder.encode(furnitureCondition, StandardCharsets.UTF_8));
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
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the furniture: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteFurniture(Integer furnitureID) {
        try {
            String deleteUrl = baseUrl + "/delete?id=" + furnitureID;

            // Send the DELETE request
            HttpResponse<String> response =  HttpRequestUtil.sendRequest("DELETE", deleteUrl);

            return response.statusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the furniture: " + e.getMessage());
            return false;
        }
    }
}
