package com.projectx.priceengine.service;

import com.projectx.priceengine.model.Product;
import com.projectx.priceengine.model.ProductSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

  static final DecimalFormat numberFormat = new DecimalFormat("#.00");

  /**
   * Process the product summary for each product
   *
   * @param products
   * @return productSummary
   */
  public ProductSummary processSummary(final List<Product> products) {
    var productSummary = new ProductSummary();
    products.forEach(product -> populateProductSummary(product, productSummary));
    // Include the same list of products in summary object
    productSummary.setProducts(products);
    log.debug("Final product summary : " + productSummary);
    return productSummary;
  }

  /**
   * Calculates the summary for the given product
   *
   * @param product
   * @param productSummary
   */
  private void populateProductSummary(final Product product, final ProductSummary productSummary) {
    try {
      if (product.getSelectedQuantity() > 0) {
        double pricePerUnit = product.getPricePerCarton() / product.getNumberOfProductsPerCarton();
        double totalPrice = product.getSelectedQuantity() * pricePerUnit;

        int numberOfCartons =
            product.getSelectedQuantity() / product.getNumberOfProductsPerCarton();

        if (numberOfCartons > 0) {
          productSummary.setDeliveryType("Home Delivery");
          if (numberOfCartons > 3) {
            totalPrice = totalPrice - (totalPrice * 10) / 100;
          }
        } else {
          productSummary.setDeliveryType("Manual Pick");
          totalPrice = totalPrice + (totalPrice * 30) / 100;
        }
        // add total price for each product in summary
        double finalTotalPrice =
            Double.parseDouble(numberFormat.format(productSummary.getTotalPrice() + totalPrice));

        productSummary.setTotalPrice(finalTotalPrice);
      }

    } catch (ArithmeticException ae) {
      log.error("Exception in ProductService while getting the product summary", ae);
      ae.printStackTrace();
    }
  }

  /**
   * Reads the list of products from a file and populates the Product model
   *
   * @return products
   */
  public List<Product> getProducts() {
    List<Product> products = null;

    try (Stream<String> lines = Files.lines(Path.of("src/main/resources/data/Product.csv"))) {
      products = getProductFromFile(lines);
      log.debug("Available products : " + products);
    } catch (IOException e) {
      log.error("Exception in ProductService while getting the products from a file", e);
      e.printStackTrace();
    }
    return products;
  }

  /**
   * Maps the product details to the product model
   *
   * @param lines
   * @return products
   */
  private List<Product> getProductFromFile(final Stream<String> lines) {
    var pattern = Pattern.compile(",");
    return lines
        .skip(1)
        .map(
            line -> {
              String[] column = pattern.split(line);
              return new Product(
                  Long.parseLong(column[0]),
                  column[1],
                  Integer.parseInt(column[2]),
                  0,
                  Double.parseDouble(column[3]),
                  Integer.parseInt(column[4]));
            })
        .collect(Collectors.toList());
  }
}
