package com.hallmanagementsys.hallmanagement.util;

public class MyUrl {
    public static final String LOCALHOST_API_URL = "http://localhost:8081/api/v1/";
    public static final String TEST_API_URL = "https://hallmanagementbackend-spring-boot-room-inventory-sys.up.railway.app/api/v1/";
    public static final String API_URL ="https://rims-backend.tywaine.me/api/v1/";

    public static String getUrl(){
        // change the returned url to whichever one you currently want to use.
        return API_URL;
        //return LOCALHOST_API_URL;
    }

    public static String getTestUrl(){
        return TEST_API_URL;
    }
}
