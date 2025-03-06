package com.hallmanagementsys.hallmanagement.service;

import com.hallmanagementsys.hallmanagement.dto.UserDTO;
import com.hallmanagementsys.hallmanagement.model.User;
import com.hallmanagementsys.hallmanagement.util.HttpStatus;
import com.hallmanagementsys.hallmanagement.util.Json;
import com.hallmanagementsys.hallmanagement.util.MyBCrypt;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Define test execution order
class UserServiceTest {
    private final LocalDateTime dateTime = LocalDateTime.now();

    private final String password = "password";
    private final UserDTO mockUserDTO = new UserDTO(null, "testUser", MyBCrypt.hashPassword(password), "ADMIN", dateTime);
    private final UserService userService = UserService.getInstance();
    private static User currentUser;


    @Test
    @Order(1)
    void sendCreateUserRequest() {
        HttpResponse<String> response = userService.sendCreateUserRequest(mockUserDTO);
        assertEquals(HttpStatus.CREATED, response.statusCode());
        UserDTO userDTO = Json.fromJson(response.body(), UserDTO.class);
        currentUser = User.fromDTO(userDTO);
        assertNotNull(currentUser);
        System.out.println(Json.prettyPrint(currentUser));
    }

    /*
    @Test
    void createUserAndRetrieve() {
    }

     */

    @Test
    @Order(2)
    void sendLoginRequest() {
        User user = userService.sendLoginRequest(mockUserDTO.getUsername(), password);
        assertNotNull(user);
    }

    @Test
    @Order(3)
    void doesAdminExist() {
        boolean exists = userService.doesAdminExist();
        assertTrue(exists);
    }

    @Test
    @Order(4)
    void fetchUserByUsername() {
        User user = userService.fetchUserByUsername("testUser");
        assertNotNull(user);
        assertEquals(currentUser.getID(), user.getID());
        assertEquals(mockUserDTO.getUsername(), user.getUsername());
        assertEquals(currentUser.getPasswordHash(), user.getPasswordHash());
        assertEquals(currentUser.getRole(), user.getRole());
    }

    @Test
    @Order(5)
    void updateUser() {
        boolean userUpdated = userService.updateUser(currentUser.getID(), "testUser2", null, null);
        assertTrue(userUpdated);
    }

    @Test
    @Order(6)
    void deleteUser() {
        boolean isDeleted = userService.deleteUser(currentUser.getID());
        assertTrue(isDeleted);
    }
}