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

    public void fetchBlocks() {
        try{
            HttpResponse<String> response = HttpRequestUtil.sendRequest("GET", baseUrl);

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
}
