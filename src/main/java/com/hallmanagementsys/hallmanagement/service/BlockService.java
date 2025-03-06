package com.hallmanagementsys.hallmanagement.service;


import com.hallmanagementsys.hallmanagement.dto.BlockDTO;
import com.hallmanagementsys.hallmanagement.model.Block;
import com.hallmanagementsys.hallmanagement.util.*;
import javafx.scene.control.Alert;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BlockService {
    private static final String baseUrl = MyUrl.getUrl() + "blocks";

    private static BlockService instance;

    private BlockService() {}

    public static synchronized BlockService getInstance() {
        if (instance == null) {
            instance = new BlockService();
        }

        return instance;
    }

    private void getBlocks(String url) {
        try{
            HttpResponse<String> response = HttpRequestUtil.sendRequest("GET", url);

            // Check the response status code
            if (response.statusCode() == HttpStatus.OK) {
                // Convert JSON response to List<BlockDTO> using the Json class
                List<BlockDTO> blockDTOS = Json.fromJsonArray(response.body(), BlockDTO.class);

                if (blockDTOS != null) {

                    // Convert DTOs to Block objects
                    for (BlockDTO blockDTO : blockDTOS) {
                        Block.fromDTO(blockDTO);
                        //System.out.println(blockDTO);
                    }

                    System.out.println("Fetched " + blockDTOS.size() + " blocks.");
                } else {
                    MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse blocks.");
                }
            }
            else {
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch blocks: " + response.body());
            }

        }catch(Exception e){
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching blocks: " + e.getMessage());
        }

    }

    public void fetchBlocks() {
        getBlocks(baseUrl);
    }

    public void fetchBlocks(Integer id) {
        // Build the URL based on the presence of ID
        String url = (id != null) ? baseUrl + "?id=" + id : baseUrl;
        getBlocks(url);
    }

    public HttpResponse<String> sendCreateBlockRequest(BlockDTO blockDTO) {
        try {
            // Convert block object to JSON
            String blockJson = Json.stringify(Json.toJson(blockDTO)); // Converts user object to JSON string
            System.out.println(blockJson);

            // Expecting CREATED RESPONSE
            return HttpRequestUtil.sendRequest(
                    "POST",
                    baseUrl,
                    Map.of("Content-Type", "application/json"),
                    blockJson
            );
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error",
                    "An error occurred while sending the create block post request: " + e.getMessage());
            return null; // Return null in case of an error
        }
    }

    public Block createBlockAndRetrieve(BlockDTO blockDTO) {
        HttpResponse<String> response = sendCreateBlockRequest(blockDTO);

        if (response == null) {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
            return null;
        }

        if (response.statusCode() == HttpStatus.CREATED) {  // Check if response is CREATED
            try {
                BlockDTO block = Json.fromJson(response.body(), BlockDTO.class);
                return Block.fromDTO(block);
                //return JsonUtil.fromJsonWithAdapter(response.body(), Block.class, new BlockTypeAdapter()); // Convert JSON to object
            } catch (Exception e) {
                e.printStackTrace();
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse block from response.");
            }
        } else {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to create block. Server responded with: " + response.body());
        }

        return null;
    }

    public boolean updateBlock(Integer blockID, Character name, Integer maxRooms) {
        try {
            String updateUrl = baseUrl + "/update/" + blockID;

            // Build the request body only with non-null parameters
            List<String> params = new ArrayList<>();

            if (name != null){
                params.add("name=" + URLEncoder.encode(name.toString(), StandardCharsets.UTF_8));
            }

            if (maxRooms != null){
                params.add("maxRooms=" + maxRooms);
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
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the block: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteBlock(Integer blockID) {
        try {
            String deleteUrl = baseUrl + "/delete?id=" + blockID;

            // Send the DELETE request. Expect NO_CONTENT Response
            HttpResponse<String> response =  HttpRequestUtil.sendRequest("DELETE", deleteUrl);

            return response.statusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the block: " + e.getMessage());
            return false;
        }
    }
}
