package com.hallmanagementsys.hallmanagement.service;

import com.hallmanagementsys.hallmanagement.dto.OccupantDTO;
import com.hallmanagementsys.hallmanagement.model.Occupant;
import com.hallmanagementsys.hallmanagement.util.HttpStatus;
import com.hallmanagementsys.hallmanagement.util.Json;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Define test execution order
class OccupantServiceTest {
    private final LocalDateTime dateTime = LocalDateTime.now();

    private final OccupantDTO mockOccupantDTO = new OccupantDTO(null, "Tywaine", "Peters", "620155443", "contact@tywaine.me", "4373555", 1, dateTime);
    private final OccupantService occupantService = OccupantService.getInstance();
    private static Occupant currentOccupant;

    @Test
    @Order(1)
    void sendCreateOccupantRequest() {
        HttpResponse<String> response = occupantService.sendCreateOccupantRequest(mockOccupantDTO);
        assertEquals(HttpStatus.CREATED, response.statusCode());
        OccupantDTO occupantDTO = Json.fromJson(response.body(), OccupantDTO.class);
        currentOccupant = Occupant.fromDTO(occupantDTO);
        assertNotNull(currentOccupant);
        System.out.println(Json.prettyPrint(currentOccupant));
    }

    /*
    @Test
    @Order(1)
    void createOccupantAndRetrieve() {
    }

     */

    @Test
    @Order(2)
    void updateOccupant() {
        boolean occupantUpdated = occupantService.updateOccupant(currentOccupant.getID(), "Jason", "Paul", null, "contact2@tywaine.me", null, 2);
        assertTrue(occupantUpdated);
    }

    @Test
    @Order(3)
    void deleteOccupant() {
        boolean isDeleted = occupantService.deleteOccupant(currentOccupant.getID());
        assertTrue(isDeleted);
    }
}