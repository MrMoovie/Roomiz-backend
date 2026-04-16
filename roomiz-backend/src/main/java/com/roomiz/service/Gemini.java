package com.roomiz.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.roomiz.entities.Product;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Gemini {

    // This pulls your Gemini key from application.properties
    private final String geminiApiKey = System.getenv("GEMINI_API");
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Used to parse JSON

    public List<Product> parseReceiptWithGemini(String mockVisionText) {
        String prompt = "You are a data extractor. I will provide raw OCR text from a Hebrew receipt. " +
                "Extract the items and prices. Ignore store names, totals, and taxes. " +
                "Return ONLY a valid JSON array of objects, where each object has 'itemName', 'amount' and 'price'. " +
                "Do not wrap the response in markdown blocks like ```json. \n\n" +
                "Raw Text:\n" + mockVisionText;

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey;        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", prompt);

        Map<String, Object> partsMap = new HashMap<>();
        partsMap.put("parts", List.of(textPart));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(partsMap));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {

            String responseStr = restTemplate.postForObject(url, entity, String.class);

            JsonNode rootNode = objectMapper.readTree(responseStr);
            JsonNode textNode = rootNode.path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text");

            String geminiResponse = textNode.asText();
            List<Product> products = objectMapper.readValue(geminiResponse, new TypeReference<List<Product>>() {
            });

            //sane check
            System.out.println("Successfully extracted " + products.size() + " products:");
            for (Product product : products) {
                System.out.println("- Name: " + product.getItemName() +
                        " | Price: " + product.getPrice() +
                        " | Qty: " + product.getAmount());
            }
            return products;

        } catch (Exception e) {
            throw new RuntimeException("Failed to talk to Gemini: " + e.getMessage());
        }
    }
}