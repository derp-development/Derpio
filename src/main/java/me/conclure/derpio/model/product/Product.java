package me.conclure.derpio.model.product;

import com.google.gson.annotations.Expose;

public final class Product {

  @Expose private String name;
  @Expose private double price;
  @Expose private String imageUrl;

  Product(String name, double price, String imageUrl) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  Product(String name, double price) {
    this(name, price, null);
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }
}
