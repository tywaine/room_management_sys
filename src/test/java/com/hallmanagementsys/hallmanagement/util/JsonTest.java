package com.hallmanagementsys.hallmanagement.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.hallmanagementsys.hallmanagement.dto.UserDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class JsonTest {

    @Test
    void testParse_ValidJson() {
        String jsonString = "{\"id\":1,\"username\":\"tpower\"}";
        JsonNode node = Json.parse(jsonString);
        assertNotNull(node);
        assertEquals(1, node.get("id").asInt());
        assertEquals("tpower", node.get("username").asText());
    }

    @Test
    void testParse_InvalidJson() {
        String invalidJson = "{id:1, username:tpower}";
        JsonNode node = Json.parse(invalidJson);
        assertNull(node);
    }

    @Test
    void testFromJson_NodeToUserDTO() {
        String json = "{\"id\":1,\"username\":\"tpower\",\"passwordHash\":\"hashed_pass\",\"role\":\"Admin\",\"createdAt\":\"2024-02-20T12:00:00\"}";
        JsonNode node = Json.parse(json);
        UserDTO userDTO = Json.fromJson(node, UserDTO.class);

        assertNotNull(userDTO);
        assertEquals(1, userDTO.getID());
        assertEquals("tpower", userDTO.getUsername());
        assertEquals("hashed_pass", userDTO.getPasswordHash());
        assertEquals("Admin", userDTO.getRole());
        assertEquals(LocalDateTime.of(2024, 2, 20, 12, 0), userDTO.getCreatedAt());
    }

    @Test
    void testFromJson_StringToUserDTO() {
        String json = "{\"id\":1,\"username\":\"tpower\",\"passwordHash\":\"hashed_pass\",\"role\":\"Admin\",\"createdAt\":\"2024-02-20T12:00:00\"}";
        UserDTO userDTO = Json.fromJson(json, UserDTO.class);

        assertNotNull(userDTO);
        assertEquals(1, userDTO.getID());
        assertEquals("tpower", userDTO.getUsername());
    }

    @Test
    void testFromJsonArray() {
        String jsonArray = "[{\"id\":1,\"username\":\"tpower\",\"passwordHash\":\"hashed_pass1\",\"role\":\"Admin\",\"createdAt\":\"2024-02-20T12:00:00\"}," +
                "{\"id\":2,\"username\":\"johndoe\",\"passwordHash\":\"hashed_pass2\",\"role\":\"User\",\"createdAt\":\"2024-02-21T15:30:00\"}]";

        List<UserDTO> userDTOS = Json.fromJsonArray(jsonArray, UserDTO.class);

        assertNotNull(userDTOS);
        assertEquals(2, userDTOS.size());
        assertEquals("tpower", userDTOS.get(0).getUsername());
        assertEquals("johndoe", userDTOS.get(1).getUsername());
    }

    @Test
    void testToJson() {
        UserDTO userDTO = new UserDTO(1, "tpower", "hashed_pass", "Admin", LocalDateTime.of(2024, 2, 20, 12, 0));
        JsonNode node = Json.toJson(userDTO);

        assertNotNull(node);
        assertEquals(1, node.get("id").asInt());
        assertEquals("tpower", node.get("username").asText());
    }

    @Test
    void testStringify() {
        UserDTO userDTO = new UserDTO(1, "tpower", "hashed_pass", "Admin", LocalDateTime.of(2024, 2, 20, 12, 0));
        JsonNode node = Json.toJson(userDTO);
        String jsonString = Json.stringify(node);

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("\"id\":1"));
    }

    @Test
    void testPrettyPrint() {
        UserDTO userDTO = new UserDTO(1, "tpower", "hashed_pass", "Admin", LocalDateTime.of(2024, 2, 20, 12, 0));
        JsonNode node = Json.toJson(userDTO);
        String jsonPretty = Json.prettyPrint(node);

        assertNotNull(jsonPretty);
        assertTrue(jsonPretty.contains("\n")); // Should contain newlines
    }

    @Test
    void testIsValidJson() {
        String validJson = "{\"id\":1,\"username\":\"tpower\"}";
        String invalidJson = "{id:1, username:tpower}";

        assertTrue(Json.isValidJson(validJson));
        assertFalse(Json.isValidJson(invalidJson));
    }
}