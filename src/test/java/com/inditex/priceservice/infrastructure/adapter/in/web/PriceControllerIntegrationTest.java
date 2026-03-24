package com.inditex.priceservice.infrastructure.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PriceControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    private static final String BASE_URL = "/api/v1/prices";

    @Test
    @DisplayName("Test 1: petición a las 10:00 del día 14 de Junio de 2020 → tarifa 1, precio 35.50")
    void test1_requestAt10_day14() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application-date", "2020-06-14T10:00:00")
                .param("product-id", "35455")
                .param("brand-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price_list").value(1))
                .andExpect(jsonPath("$.price").value(35.50))
                .andExpect(jsonPath("$.product_id").value(35455))
                .andExpect(jsonPath("$.brand_id").value(1))
                .andExpect(jsonPath("$.start_date").exists())
                .andExpect(jsonPath("$.end_date").exists())
                .andExpect(jsonPath("$.currency").exists());
    }

    @Test
    @DisplayName("Test 2: petición a las 16:00 del día 14 de Junio de 2020 → tarifa 2, precio 25.45")
    void test2_requestAt16_day14() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application-date", "2020-06-14T16:00:00")
                .param("product-id", "35455")
                .param("brand-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price_list").value(2))
                .andExpect(jsonPath("$.price").value(25.45))
                .andExpect(jsonPath("$.product_id").value(35455))
                .andExpect(jsonPath("$.brand_id").value(1));
    }

    @Test
    @DisplayName("Test 3: petición a las 21:00 del día 14 de Junio de 2020 → tarifa 1, precio 35.50")
    void test3_requestAt21_day14() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application-date", "2020-06-14T21:00:00")
                .param("product-id", "35455")
                .param("brand-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price_list").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    @DisplayName("Test 4: petición a las 10:00 del día 15 de Junio de 2020 → tarifa 3, precio 30.50")
    void test4_requestAt10_day15() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application-date", "2020-06-15T10:00:00")
                .param("product-id", "35455")
                .param("brand-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price_list").value(3))
                .andExpect(jsonPath("$.price").value(30.50));
    }

    @Test
    @DisplayName("Test 5: petición a las 21:00 del día 16 de Junio de 2020 → tarifa 4, precio 38.95")
    void test5_requestAt21_day16() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application-date", "2020-06-16T21:00:00")
                .param("product-id", "35455")
                .param("brand-id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price_list").value(4))
                .andExpect(jsonPath("$.price").value(38.95));
    }

    @Test
    @DisplayName("Test 6: parámetro obligatorio ausente → 400 Bad Request con mensaje original")
    void test6_missingParameter_returnsBadRequest() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("product-id", "35455")
                .param("brand-id", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    @DisplayName("Test 7: tipo de parámetro inválido → 400 Bad Request")
    void test7_invalidParameterType_returnsBadRequest() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application-date", "invalid-date")
                .param("product-id", "35455")
                .param("brand-id", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    @DisplayName("Test 8: producto inexistente → 404 Not Found con ProblemDetail")
    void test8_notFound_returnsProblemDetail() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application-date", "2020-06-14T10:00:00")
                .param("product-id", "99999")
                .param("brand-id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("https://tools.ietf.org/html/rfc9457"))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("resource_not_found"))
                .andExpect(jsonPath("$.request_id").exists())
                .andExpect(jsonPath("$.instance").value("/api/v1/prices"));
    }

    @Test
    @DisplayName("Test 9: brand_id inválido (negativo) → 400 Bad Request por IllegalArgumentException")
    void test9_invalidBrandId_returnsBadRequest() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application-date", "2020-06-14T10:00:00")
                .param("product-id", "35455")
                .param("brand-id", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("invalid_argument"));
    }
}
