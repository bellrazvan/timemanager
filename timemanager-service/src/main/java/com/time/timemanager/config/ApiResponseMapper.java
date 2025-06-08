package com.time.timemanager.config;

import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class ApiResponseMapper {
    public Map<String, String> errorResponse(String message) {
        return Map.of("error", message);
    }

    public Map<String, String> successfulResponse(String message) {
        return Map.of("success", message);
    }

    public Map<String, String> accessTokenResponse(String token) {
        return Map.of("accessToken", token);
    }
}
