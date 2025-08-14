package com.vibesort;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * VibeSort - AI-powered sorting library using OpenAI API
 * 
 * This class provides intelligent sorting capabilities by leveraging
 * OpenAI's language models to understand and sort data based on context.
 */
public class Sort {
    private static final String DEFAULT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    // Environment variable names
    private static final String ENV_API_KEY = "OPENAI_API_KEY";
    private static final String ENV_MODEL = "OPENAI_MODEL";
    private static final String ENV_BASE_URL = "OPENAI_BASE_URL";
    
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;
    private final String baseUrl;
    
    /**
     * Default constructor - uses environment variables for configuration
     * Environment variables:
     * - OPENAI_API_KEY: API key (required)
     * - OPENAI_MODEL: Model name (optional, defaults to "gpt-3.5-turbo")
     * - OPENAI_BASE_URL: Base URL (optional, defaults to OpenAI API URL)
     */
    public Sort() {
        this(System.getenv(ENV_API_KEY), 
             System.getenv(ENV_MODEL), 
             System.getenv(ENV_BASE_URL));
    }
    
    /**
     * Constructor with API key
     * @param apiKey OpenAI API key
     */
    public Sort(String apiKey) {
        this(apiKey, "gpt-3.5-turbo");
    }
    
    /**
     * Constructor with API key and model
     * @param apiKey OpenAI API key
     * @param model OpenAI model to use (e.g., "gpt-3.5-turbo", "gpt-4")
     */
    public Sort(String apiKey, String model) {
        this(apiKey, model, null);
    }
    
    /**
     * Constructor with API key, model and base URL
     * @param apiKey OpenAI API key
     * @param model OpenAI model to use (e.g., "gpt-3.5-turbo", "gpt-4")
     * @param baseUrl Base URL for API calls (null to use default)
     */
    public Sort(String apiKey, String model, String baseUrl) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key cannot be null or empty. Please set OPENAI_API_KEY environment variable or provide it directly.");
        }
        
        this.apiKey = apiKey;
        this.model = model != null ? model : "gpt-3.5-turbo";
        this.baseUrl = baseUrl != null ? baseUrl : DEFAULT_API_URL;
        
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Sort a list of strings using AI
     * @param items List of items to sort
     * @param criteria Sorting criteria (e.g., "alphabetically", "by length", "by importance")
     * @return Sorted list of items
     * @throws IOException if API call fails
     */
    public List<String> sort(List<String> items, String criteria) throws IOException {
        String prompt = buildSortPrompt(items, criteria);
        String response = callOpenAI(prompt);
        return parseSortedItems(response);
    }
    
    /**
     * Sort a list of strings alphabetically using AI
     * @param items List of items to sort
     * @return Sorted list of items
     * @throws IOException if API call fails
     */
    public List<String> sort(List<String> items) throws IOException {
        return sort(items, "alphabetically");
    }
    
    private String buildSortPrompt(List<String> items, String criteria) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Please sort the following items ").append(criteria).append(".\n");
        prompt.append("Return only the sorted items, one per line, without any additional text or numbering.\n\n");
        prompt.append("Items to sort:\n");
        for (String item : items) {
            prompt.append("- ").append(item).append("\n");
        }
        return prompt.toString();
    }
    
    private String callOpenAI(String prompt) throws IOException {
        // 使用 Jackson 构建 JSON 请求体
        var requestData = objectMapper.createObjectNode();
        requestData.put("model", model);
        requestData.put("max_tokens", 1000);
        
        var messagesArray = objectMapper.createArrayNode();
        var messageObject = objectMapper.createObjectNode();
        messageObject.put("role", "user");
        messageObject.put("content", prompt);
        messagesArray.add(messageObject);
        requestData.set("messages", messagesArray);
        
        String jsonBody = objectMapper.writeValueAsString(requestData);
        
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(baseUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .post(body)
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                throw new IOException("OpenAI API call failed: " + response.code() + " " + response.message() + ". Response: " + responseBody);
            }
            
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("choices").get(0).get("message").get("content").asText();
        }
    }
    
    private List<String> parseSortedItems(String response) {
        return Arrays.stream(response.trim().split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.replaceAll("^[-*•]\\s*", "")) // Remove bullet points
                .collect(Collectors.toList());
    }
}