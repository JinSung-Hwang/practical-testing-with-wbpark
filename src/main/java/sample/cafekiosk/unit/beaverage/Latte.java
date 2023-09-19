package sample.cafekiosk.unit.beaverage;

import sample.cafekiosk.unit.beaverage.Beaverage;

public class Latte implements Beaverage {
  @Override
  public String getName() {
    return "Latte";
  }

  @Override
  public int getPrice() {
    return 2000;
  }
}
