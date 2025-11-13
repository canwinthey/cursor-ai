package com.cursor.product.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CorsConfigTest {

    @Autowired
    private CorsConfig corsConfig;

    @Test
    void testCorsFilter() {
        CorsFilter corsFilter = corsConfig.corsFilter();

        assertNotNull(corsFilter);
    }

    @Test
    void testCorsFilterBeanCreation() {
        CorsFilter corsFilter = corsConfig.corsFilter();
        
        assertNotNull(corsFilter);
        assertTrue(corsFilter instanceof CorsFilter);
    }
}

