package com.hallmanagementsys.hallmanagement.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.hallmanagementsys.hallmanagement.dto.UserDTO;
import com.hallmanagementsys.hallmanagement.util.*;
import com.hallmanagementsys.hallmanagement.model.User;
import javafx.scene.control.Alert;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserService {
    private static final String baseUrl = MyUrl.getUrl() + "users";
    private static UserService instance;

    private UserService() {}

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }

        return instance;
    }

    public User sendLoginRequest(String username, String password) {
        try {
            String loginUrl = baseUrl + "/login";

            // Create a query string
            String requestBody = String.format("username=%s&password=%s", username, password);

            // Expect OK Response
            HttpResponse<String> response = HttpRequestUtil.sendRequest("POST", loginUrl, Map.of("Content-Type", "application/x-www-form-urlencoded"), requestBody);

            // Check the response status code
            if (response.statusCode() == HttpStatus.OK) { // Expecting Ok
                // Convert JSON response to User
                UserDTO userDTO = Json.fromJson(response.body(), UserDTO.class);
                return User.fromDTO(userDTO);
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean doesAdminExist() {
        try {
            // Build the URL
            String doesAdminExistUrl = baseUrl + "/admin/exists";

            // Send the request
            HttpResponse<String> response = HttpRequestUtil.sendRequest("GET", doesAdminExistUrl);

            // Check the response status code
            if (response.statusCode() == HttpStatus.OK) { // Expecting 200 OK

                // Parse JSON using Jackson
                JsonNode jsonResponse = Json.parse(response.body());

                // Extract the "exists" boolean value
                boolean exists = jsonResponse.get("exists").asBoolean();

                System.out.println("Admin exists: " + exists);
                return exists;
            } else {
                System.out.println("Unexpected response: " + response.body());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while checking admin existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fetches a user by their username.
     *
     * @param username The username of the user to fetch.
     * @return The User object or null if not found or in case of an error.
     */
    public User fetchUserByUsername(String username) {
        try {
            // Build the URL
            String usernameUrl = baseUrl + "/username/" + username;

            // Send the request
            HttpResponse<String> response = HttpRequestUtil.sendRequest("GET", usernameUrl);

            // Check the response status code
            if (response.statusCode() == HttpStatus.OK) { // Expecting Ok
                // Convert JSON response to User
                UserDTO userDTO = Json.fromJson(response.body(), UserDTO.class);
                return User.fromDTO(userDTO);
            }
            else if (response.statusCode() == HttpStatus.NOT_FOUND) {
                MyAlert.showAlert(Alert.AlertType.INFORMATION, "Not Found", "User not found.");
                return null;
            }
            else {
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch user: " + response.body());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching the user: " + e.getMessage());
            return null;
        }
    }

    private void getUsers(String url){
        try {
            // Send the request
            HttpResponse<String> response = HttpRequestUtil.sendRequest("GET", url);

            // Check the response status code
            if (response.statusCode() == HttpStatus.OK) {
                // Convert JSON response to List<UserDTO> using the Json class
                List<UserDTO> userDTOS = Json.fromJsonArray(response.body(), UserDTO.class);

                if (userDTOS != null) {

                    // Convert DTOs to User objects
                    for (UserDTO userDTO : userDTOS) {
                        User.fromDTO(userDTO);
                        //System.out.println(userDTO);
                    }

                    System.out.println("Fetched " + userDTOS.size() + " users.");
                }
                else {
                    MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse users.");
                }
            }
            else {
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch users: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while fetching users: " + e.getMessage());
        }
    }

    public void fetchUsers() {
        getUsers(baseUrl);
    }

    public void fetchUsers(Integer id) {
        // Build the URL based on the presence of ID
        String url = (id != null) ? baseUrl + "?id=" + id : baseUrl;
        getUsers(url);
    }

    public HttpResponse<String> sendCreateUserRequest(UserDTO userDTO) {
        try {
            // Convert user object to JSON using Jackson
            String userJson = Json.stringify(Json.toJson(userDTO));
            System.out.println(Json.prettyPrint(userDTO));

            // Send the HTTP POST request
            return HttpRequestUtil.sendRequest(
                    "POST",
                    baseUrl,
                    Map.of("Content-Type", "application/json"),
                    userJson
            );
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error",
                    "An error occurred while sending the create user post request: " + e.getMessage());
            return null; // Return null in case of an error
        }
    }

    public User createUserAndRetrieve(UserDTO userDTO) {
        HttpResponse<String> response = sendCreateUserRequest(userDTO);

        if (response == null) {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "No response from server.");
            return null;
        }

        if (response.statusCode() == HttpStatus.CREATED) {  // Check if response is CREATED
            try {
                UserDTO user = Json.fromJson(response.body(), UserDTO.class);
                return User.fromDTO(user);
            } catch (Exception e) {
                e.printStackTrace();
                MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to parse user from response.");
            }
        } else {
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "Failed to create user. Server responded with: " + response.body());
        }

        return null;
    }

    public boolean updateUser(Integer userID, String username, String passwordHash, String role) {
        try {
            String updateUrl = baseUrl + "/update/" + userID;

            // Build the request body only with non-null parameters
            List<String> params = new ArrayList<>();
            if (username != null){
                params.add("username=" + URLEncoder.encode(username, StandardCharsets.UTF_8));
            }

            if (passwordHash != null){
                params.add("passwordHash=" + URLEncoder.encode(passwordHash, StandardCharsets.UTF_8));
            }

            if (role != null){
                params.add("role=" + URLEncoder.encode(role, StandardCharsets.UTF_8));
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
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while updating the user: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser(Integer userID) {
        try {
            String deleteUrl = baseUrl + "/delete?id=" + userID;

            // Send the DELETE request. Expect NO_CONTENT Response
            HttpResponse<String> response =  HttpRequestUtil.sendRequest("DELETE", deleteUrl);

            return response.statusCode() == HttpStatus.NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
            MyAlert.showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the user: " + e.getMessage());
            return false;
        }
    }
}
