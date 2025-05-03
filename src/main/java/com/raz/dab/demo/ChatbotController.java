package com.raz.dab.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This controller handles chat requests and responses.
 */
@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    /**
     * Endpoint to handle chat requests.
     * This endpoint receives the user's message and returns the chatbot's response.
     *
     * @param sessionId   The session ID for the user.
     * @param userMessage The user's message.
     * @return The chatbot's response.
     */
    @PostMapping("/{sessionId}")
    public String getChatResponse(@PathVariable String sessionId, @RequestBody String userMessage) {
        // Pass the user message to the service to get a response
        String botResponse = chatbotService.getChatbotResponse(sessionId, userMessage);
        return botResponse; // Send back the response from the service
    }
}
