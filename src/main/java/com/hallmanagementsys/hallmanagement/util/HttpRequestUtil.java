package com.hallmanagementsys.hallmanagement.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpRequestUtil {

    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * Generic method to send HTTP requests (GET, POST, PUT, DELETE).
     *
     * @param method The HTTP method (GET, POST, PUT, DELETE)
     * @param url The URL for the request
     * @param body The request body (optional, for POST and PUT requests)
     * @param headers The request headers (optional)
     * @return The HTTP response
     */
    public static HttpResponse<String> sendRequest(String method, String url, Map<String, String> headers, String body) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url));

            // Set HTTP method (GET, POST, PUT, DELETE)
            switch (method.toUpperCase()) {
                case "POST":
                    requestBuilder.POST(body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
                    break;
                case "PUT":
                    requestBuilder.PUT(body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body));
                    break;
                case "DELETE":
                    requestBuilder.DELETE();
                    break;
                default:
                    requestBuilder.GET();
                    break;
            }

            // Add headers if provided
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(requestBuilder::header);
            }

            HttpRequest request = requestBuilder.build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> sendRequest(String method, String url, Map<String, String> headers) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url));

            // Set HTTP method (GET, DELETE)
            if (method.equalsIgnoreCase("DELETE")) {
                requestBuilder.DELETE();
            }
            else {
                requestBuilder.GET();
            }

            // Add headers if provided
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(requestBuilder::header);
            }

            HttpRequest request = requestBuilder.build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HttpResponse<String> sendRequest(String method, String url) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url));

            // Set HTTP method (GET, DELETE)
            if (method.equalsIgnoreCase("DELETE")) {
                requestBuilder.DELETE();
            }
            else {
                requestBuilder.GET();
            }

            HttpRequest request = requestBuilder.build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
