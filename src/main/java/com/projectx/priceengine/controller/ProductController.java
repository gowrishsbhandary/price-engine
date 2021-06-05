package com.projectx.priceengine.controller;

import com.projectx.priceengine.model.Product;
import com.projectx.priceengine.model.ProductSummary;
import com.projectx.priceengine.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/price-engine")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "Gets list of products from the system")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Got the list of products",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = Product.class))
            }),
        @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Products not found", content = @Content)
      })
  @GetMapping(value = "/products")
  public List<Product> getProducts() {
    return productService.getProducts();
  }

  @Operation(summary = "Processes the summary for given products")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Summary is processed",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ProductSummary.class))
            }),
        @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content),
        @ApiResponse(
            responseCode = "404",
            description = "Product summary not found",
            content = @Content)
      })
  @PostMapping(value = "/summary")
  public ProductSummary processSummary(@Valid @RequestBody List<Product> products) {
    return productService.processSummary(products);
  }
}
