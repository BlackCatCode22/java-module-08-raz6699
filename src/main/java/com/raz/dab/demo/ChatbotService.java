package com.raz.dab.demo;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ChatbotService handles communication with OpenRouter API and maintains session-based chat history.
 */
@Service
public class ChatbotService {

    // Inject API key and URL from application properties
    @Value("${openai.api.key}")
    private String apiKey;

    // Changed URL to OpenRouter API URL
    @Value("${openai.api.url:https://openrouter.ai/api/v1/chat/completions}")
    private String apiUrl;

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // Map to store conversation history per session
    private final Map<String, List<Map<String, String>>> sessionConversations = new HashMap<>();

    /**
     * Retrieves a response from the chatbot for the given session and user message.
     *
     * @param sessionId   The unique session ID for the user.
     * @param userMessage The user's message to the chatbot.
     * @return The chatbot's response.
     */
    public String getChatbotResponse(String sessionId, String userMessage) {
        OkHttpClient client = new OkHttpClient();

        List<Map<String, String>> conversationHistory = sessionConversations.computeIfAbsent(sessionId, k -> new ArrayList<>());

        Map<String, String> userEntry = new HashMap<>();
        userEntry.put("role", "user");
        userEntry.put("content", userMessage);
        conversationHistory.add(userEntry);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payload = new HashMap<>();
            // Updated the model to a free OpenAI model: GPT-3.5-turbo
            payload.put("model", "gpt-3.5-turbo");  // Free OpenAI model
            payload.put("messages", conversationHistory);

            String requestBody = objectMapper.writeValueAsString(payload);

            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(RequestBody.create(requestBody, JSON))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                int code = response.code();
                String responseBody = response.body() != null ? response.body().string() : "";

                if (code == 429) {
                    System.err.println("Quota error (429): " + responseBody);
                    return "Error: You've exceeded your OpenAI API quota. Please check your usage and billing.";
                } else if (code == 402) {
                    System.err.println("Payment error (402): " + responseBody);
                    return "Error: Insufficient credits. Please visit https://openrouter.ai/settings/credits to add more credits.";
                } else if (!response.isSuccessful()) {
                    System.err.println("API request failed.");
                    System.err.println("Request Body: " + requestBody);
                    System.err.println("Response Code: " + code);
                    System.err.println("Response Body: " + responseBody);
                    return "Error: API request failed with code " + code + " - " + response.message();
                }

                String botReply = extractBotMessage(responseBody);

                Map<String, String> botEntry = new HashMap<>();
                botEntry.put("role", "assistant");
                botEntry.put("content", botReply);
                conversationHistory.add(botEntry);

                return botReply;
            }
        } catch (Exception e) {
            return "Error: Communication issue with OpenRouter API - " + e.getMessage();
        }
    }

    /**
     * Extracts the chatbot's message from the API response.
     *
     * @param responseBody The JSON response from OpenRouter API.
     * @return The chatbot's message content.
     */
    private String extractBotMessage(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode choicesNode = rootNode.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode messageNode = choicesNode.get(0).path("message");
                return messageNode.path("content").asText();
            } else {
                return "Error: No choices found in the response.";
            }
        } catch (Exception e) {
            return "Error: Unable to parse response - " + e.getMessage();
        }
    }

    /**
     * Clears the conversation history for the given session ID.
     *
     * @param sessionId The session ID whose history should be cleared.
     */
    public void clearConversation(String sessionId) {
        sessionConversations.remove(sessionId);
    }
}
