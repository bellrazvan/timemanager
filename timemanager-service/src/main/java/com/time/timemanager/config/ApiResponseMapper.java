package com.time.timemanager.config;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class ApiResponseMapper {
    public Map<String, String> errorResponse(String errorMessage) {
        return Map.of("error", errorMessage);
    }

    public Map<String, String> successfulResponse(String message) {
        return Map.of("message", message);
    }

    public Map<String, String> accessTokenResponse(String token) {
        return Map.of("accessToken", token);
    }
}
