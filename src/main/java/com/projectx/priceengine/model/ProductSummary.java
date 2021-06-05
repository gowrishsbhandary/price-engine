package com.projectx.priceengine.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductSummary {
  @NotNull
  private double totalPrice;
  @NotNull
  private String deliveryType;
  @NotNull
  private List<Product> products;
}
