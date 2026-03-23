package com.inditex.priceservice.infrastructure.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
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

    private final String BASE_URL = "/api/prices/v1";

    @Test
    void test1_requestAt10_day14() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application_date", "2020-06-14T10:00:00")
                .param("product_id", "35455")
                .param("brand_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.business_object[0].price_list").value(1))
                .andExpect(jsonPath("$.business_object[0].price").value(35.50));
    }

    @Test
    void test2_requestAt16_day14() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application_date", "2020-06-14T16:00:00")
                .param("product_id", "35455")
                .param("brand_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.business_object[0].price_list").value(2))
                .andExpect(jsonPath("$.business_object[0].price").value(25.45));
    }

    @Test
    void test3_requestAt21_day14() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application_date", "2020-06-14T21:00:00")
                .param("product_id", "35455")
                .param("brand_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.business_object[0].price_list").value(1))
                .andExpect(jsonPath("$.business_object[0].price").value(35.50));
    }

    @Test
    void test4_requestAt10_day15() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application_date", "2020-06-15T10:00:00")
                .param("product_id", "35455")
                .param("brand_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.business_object[0].price_list").value(3))
                .andExpect(jsonPath("$.business_object[0].price").value(30.50));
    }

    @Test
    void test5_requestAt21_day16() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application_date", "2020-06-16T21:00:00")
                .param("product_id", "35455")
                .param("brand_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.business_object[0].price_list").value(4))
                .andExpect(jsonPath("$.business_object[0].price").value(38.95));
    }

    @Test
    void test6_missingParameter_returnsProblemDetail() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("product_id", "35455")
                .param("brand_id", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Required parameter 'application_date' is not present."))
                .andExpect(jsonPath("$.instance").value(BASE_URL));
    }

    @Test
    void test7_invalidParameterType_returnsProblemDetail() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application_date", "invalid-date")
                .param("product_id", "35455")
                .param("brand_id", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").value(BASE_URL));
    }

    @Test
    void test8_notFound_returnsProblemDetail() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .param("application_date", "2020-06-14T10:00:00")
                .param("product_id", "99999")
                .param("brand_id", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("https://tools.ietf.org/html/rfc9457"))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("resource_not_found"))
                .andExpect(jsonPath("$.request_id").exists());
    }
}
