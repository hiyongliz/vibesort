package com.vibesort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {
    private Sort sort;
    
    @BeforeEach
    void setUp() {
        // Use environment variable for API key in tests
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey != null && !apiKey.isEmpty()) {
            try {
                sort = new Sort(); // Use environment variables
            } catch (IllegalArgumentException e) {
                // Fallback to manual construction if env vars not set properly
                sort = new Sort(apiKey);
            }
        }
    }
    
    @Test
    @EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
    void testBasicSort() throws IOException {
        List<String> items = Arrays.asList("zebra", "apple", "banana", "cherry");
        List<String> sorted = sort.sort(items);
        
        assertNotNull(sorted);
        assertEquals(4, sorted.size());
        // Note: Actual sorting verification would depend on AI response
    }
    
    @Test
    @EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
    void testSortWithCriteria() throws IOException {
        List<String> items = Arrays.asList("cat", "elephant", "dog", "butterfly");
        List<String> sorted = sort.sort(items, "by length from shortest to longest");
        
        assertNotNull(sorted);
        assertEquals(4, sorted.size());
    }
    
    @Test
    void testConstructor() {
        assertDoesNotThrow(() -> new Sort("test-key"));
        assertDoesNotThrow(() -> new Sort("test-key", "gpt-4"));
        assertDoesNotThrow(() -> new Sort("test-key", "gpt-4", "https://api.openai.com/v1/chat/completions"));
        
        // Test validation
        assertThrows(IllegalArgumentException.class, () -> new Sort(null));
        assertThrows(IllegalArgumentException.class, () -> new Sort(""));
        assertThrows(IllegalArgumentException.class, () -> new Sort("  "));
    }
}