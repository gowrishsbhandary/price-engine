package com.projectx.priceengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectx.priceengine.controller.ProductController;
import com.projectx.priceengine.model.Product;
import com.projectx.priceengine.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PriceEngineApplicationTests {

  @InjectMocks private ProductController productController;

  @Mock private ProductService productService;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void Test_getProduct_expect_success() throws Exception {
    Mockito.when(productService.getProducts()).thenReturn(new ArrayList<>());
    mockMvc.perform(get("/price-engine/products")).andExpect(status().isOk());
  }

  @Test
  void Test_processSummary_expect_success() throws Exception {
    Product product = new Product(1001, "Apple", 500, 10, 175, 20);
    List<Product> products = new ArrayList<>(Arrays.asList(product));
    Mockito.when(productService.processSummary(Mockito.any())).thenReturn(Mockito.any());
    mockMvc
        .perform(
            post("/price-engine/summary")
                .content(objectMapper.writeValueAsString(products))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }
}
