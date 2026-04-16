package com.roomiz.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Vision {
    private final String apiKey = System.getenv("VISION_API");

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String extractTextFromImage(String base64Image) {
        String url = "https://vision.googleapis.com/v1/images:annotate?key=" + apiKey;

        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("content", base64Image);

        Map<String, Object> featureMap = new HashMap<>();
        featureMap.put("type", "TEXT_DETECTION");

        Map<String, Object> request = new HashMap<>();
        request.put("image", imageMap);
        request.put("features", List.of(featureMap));

        requestBody.put("requests", List.of(request));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {

            String responseStr = restTemplate.postForObject(url, entity, String.class);

            JsonNode root = objectMapper.readTree(responseStr);
            JsonNode textAnnotations = root.path("responses").get(0).path("textAnnotations");

            if (textAnnotations.isMissingNode() || textAnnotations.isEmpty()) {
                System.out.println("Vision API found no text in this image.");
                return "";
            }

            return textAnnotations.get(0).path("description").asText();

        } catch (Exception e) {
            throw new RuntimeException("Failed to communicate with Google Vision API: " + e.getMessage());
        }
    }
}
