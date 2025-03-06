package com.hallmanagementsys.hallmanagement.util;

public class MyUrl {
    public static final String LOCAL_API_URL = "http://localhost:8081/api/v1/";

    public static final String API_URL = "https://hallmanagementbackend-spring-boot-room-inventory-sys.up.railway.app/api/v1/";

    public static final String WEBSOCKET_API_URL ="https://room-inventory-sysbackend-production.up.railway.app/api/v1/";

    public static String getUrl(){
        // change the returned url to whichever one you currently want to use.
        return WEBSOCKET_API_URL;
    }


}
