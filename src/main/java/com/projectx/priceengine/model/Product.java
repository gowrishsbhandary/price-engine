package com.projectx.priceengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  @NotNull private long productId;
  @NotNull private String productName;
  @NotNull private int quantityAvailable;
  @NotNull private int selectedQuantity;
  private double pricePerCarton;
  private int numberOfProductsPerCarton;
}
