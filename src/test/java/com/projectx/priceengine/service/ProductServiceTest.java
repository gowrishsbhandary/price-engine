package com.projectx.priceengine.service;

import com.projectx.priceengine.model.Product;
import com.projectx.priceengine.model.ProductSummary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ProductServiceTest {

  @InjectMocks private ProductService productService;

  @Test
  void getProductsTest() {
    List<Product> products = productService.getProducts();
    Assertions.assertNotNull(products);
    Assertions.assertEquals(5, products.size());
  }

  @Test
  void Test_processSummary_with_two_products_expect_valid_productSummary() {
    Product product1 = new Product(1001, "Apple", 500, 20, 175, 20);
    Product product2 = new Product(1001, "Orange", 100, 10, 825, 5);
    List<Product> products = new ArrayList<Product>(Arrays.asList(product1, product2));
    ProductSummary productSummary = productService.processSummary(products);
    Assertions.assertNotNull(productSummary);
    Assertions.assertEquals(1825.0, productSummary.getTotalPrice());
    Assertions.assertEquals("Home Delivery", productSummary.getDeliveryType());
    Assertions.assertNotNull(productSummary.getProducts());
    Assertions.assertEquals(2, productSummary.getProducts().size());
  }

  @Test
  void Test_processSummary_with_product_lessThan_carton_expect_30_percent_morePrice() {
    Product product1 = new Product(1001, "Apple", 500, 10, 175, 20);
    List<Product> products = new ArrayList<Product>(Arrays.asList(product1));
    ProductSummary productSummary = productService.processSummary(products);
    Assertions.assertNotNull(productSummary);
    Assertions.assertEquals(113.75, productSummary.getTotalPrice());
    Assertions.assertEquals("Manual Pick", productSummary.getDeliveryType());
  }

  @Test
  void Test_processSummary_with_product_moreThan_3_cartons_expect_10_percent_lessPrice() {
    Product product2 = new Product(1001, "Orange", 100, 20, 825, 5);
    List<Product> products = new ArrayList<Product>(Arrays.asList(product2));
    ProductSummary productSummary = productService.processSummary(products);
    Assertions.assertNotNull(productSummary);
    Assertions.assertEquals(2970.0, productSummary.getTotalPrice());
  }
}
