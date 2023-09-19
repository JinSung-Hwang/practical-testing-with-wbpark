package sample.cafekiosk.unit.beaverage;

public class Americano implements Beaverage{
  @Override
  public String getName() {
    return "Americano";
  }

  @Override
  public int getPrice() {
    return 1500;
  }
}
