package com.hallmanagementsys.hallmanagement.service;

import com.hallmanagementsys.hallmanagement.dto.FurnitureDTO;
import com.hallmanagementsys.hallmanagement.model.Furniture;
import com.hallmanagementsys.hallmanagement.util.HttpStatus;
import com.hallmanagementsys.hallmanagement.util.Json;
import org.junit.jupiter.api.Test;

import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class FurnitureServiceTest {
    private final FurnitureDTO mockfurnitureDTO = new FurnitureDTO(null, 1, "couch", "EXCELLENT");
    private final FurnitureService furnitureService = FurnitureService.getInstance();
    private static Furniture currentFurniture;

    @Test
    void sendCreateFurnitureRequest() {
        HttpResponse<String> response = furnitureService.sendCreateFurnitureRequest(mockfurnitureDTO);
        assertEquals(HttpStatus.CREATED, response.statusCode());
        FurnitureDTO furnitureDTO = Json.fromJson(response.body(), FurnitureDTO.class);
        currentFurniture = Furniture.fromDTO(furnitureDTO);
        assertNotNull(currentFurniture);
        System.out.println(Json.prettyPrint(currentFurniture));
    }

    /*
    @Test
    void createFurnitureAndRetrieve() {

    }

     */

    @Test
    void updateFurniture() {
        boolean furnitureUpdated = furnitureService.updateFurniture(currentFurniture.getID(), 2, "Bed", "POOR");
        assertTrue(furnitureUpdated);
    }

    @Test
    void deleteFurniture() {
        boolean isDeleted = furnitureService.deleteFurniture(currentFurniture.getID());
        assertTrue(isDeleted);
    }
}