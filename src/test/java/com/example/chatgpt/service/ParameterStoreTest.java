package com.example.chatgpt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ParameterStoreTest {

    @Value("${apikey}")
    private String openAiValue;

    @Test
    public void testLoadParameterFromStore() {
        assertNotNull(openAiValue);
    }
}