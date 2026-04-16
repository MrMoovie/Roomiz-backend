package com.roomiz.service;

import com.roomiz.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Scanner {

    @Autowired
    private Gemini gemini;

    @Autowired
    private Vision vision; // Inject the new service!

    public List<Product> scan(String base64Image) {
        System.out.println("1. Sending image to Google Vision API...");

        // 1. Extract the raw text from the image
        String rawText = vision.extractTextFromImage(base64Image);

        if (rawText == null || rawText.trim().isEmpty()) {
            throw new RuntimeException("Could not read any text from the provided image.");
        }
        System.out.println("[>>>] raw response:\n" + rawText + "\n");

        System.out.println("2. Vision success! Extracted text length: " + rawText.length());
        System.out.println("3. Sending text to Gemini AI for structuring...");

        // 2. Send the raw text to Gemini to turn it into a List of Products
        return gemini.parseReceiptWithGemini(rawText);
    }
}