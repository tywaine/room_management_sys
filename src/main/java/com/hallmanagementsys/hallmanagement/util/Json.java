package com.hallmanagementsys.hallmanagement.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Json {
    private static final ObjectMapper objectMapper = getDefaultObjectMapper();
    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ISO_DATE_TIME_FORMATTER));

        defaultObjectMapper.registerModule(javaTimeModule);
        defaultObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return defaultObjectMapper;
    }

    public static JsonNode parse(String src){
        try{
            return objectMapper.readTree(src);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

    }

    public static <A> A fromJson(JsonNode node, Class<A> clazz) {
        try {
            return objectMapper.treeToValue(node, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <A> A fromJson(String json, Class<A> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <A> List<A> fromJsonArray(String jsonArray, Class<A> clazz) {
        try {
            return objectMapper.readValue(jsonArray,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <A> List<A> fromJsonArray(JsonNode node, Class<A> clazz) {
        try {
            return objectMapper.readValue(
                    objectMapper.treeAsTokens(node),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonNode toJson(Object obj){
        return objectMapper.valueToTree(obj);
    }

    public static String stringify(JsonNode node){
        return generateString(node, false);
    }

    public static String prettyPrint(JsonNode node){
        return generateString(node, true);
    }

    public static String prettyPrint(Object obj){
        return generateString(toJson(obj), true);
    }

    private static String generateString(JsonNode node, boolean pretty){
        ObjectWriter objectWriter = objectMapper.writer();
        if(pretty){
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }

        try{
            return objectWriter.writeValueAsString(node);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
