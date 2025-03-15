module com.hallmanagementsys.hallmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires jbcrypt;
    requires java.prefs;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.github.librepdf.openpdf;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires org.apache.logging.log4j;
    requires java.logging;
    // Spring Framework modules
    requires spring.websocket;
    requires spring.messaging;
    requires spring.core;

    // WebSocket related modules
    requires jakarta.websocket.client;
    requires jakarta.websocket;

    opens com.hallmanagementsys.hallmanagement to javafx.fxml;
    exports com.hallmanagementsys.hallmanagement;
    exports com.hallmanagementsys.hallmanagement.controller;
    exports com.hallmanagementsys.hallmanagement.controller.admin;
    exports com.hallmanagementsys.hallmanagement.controller.staff;
    exports com.hallmanagementsys.hallmanagement.dto;
    exports com.hallmanagementsys.hallmanagement.enums;
    exports com.hallmanagementsys.hallmanagement.util;
    exports com.hallmanagementsys.hallmanagement.model;
    exports com.hallmanagementsys.hallmanagement.service;
    exports com.hallmanagementsys.hallmanagement.viewFactory;
    exports com.hallmanagementsys.hallmanagement.websocket;
    exports com.hallmanagementsys.hallmanagement.dto.msg;
}