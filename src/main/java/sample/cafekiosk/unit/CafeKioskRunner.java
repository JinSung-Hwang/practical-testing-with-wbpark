package sample.cafekiosk.unit;

import sample.cafekiosk.unit.beaverage.Americano;
import sample.cafekiosk.unit.beaverage.Latte;

public class CafeKioskRunner {
  public static void main(String[] args) {
    CafeKiosk cafeKiosk = new CafeKiosk();

    Americano americano = new Americano();
    cafeKiosk.add(americano);
    System.out.println("아메리카노 추가");
    cafeKiosk.add(new Latte());
    System.out.println("라뗴 추가");

    System.out.println("총 가격: " + cafeKiosk.calculateTotalPrice());

    cafeKiosk.remove(americano);
    System.out.println("아메리카노 제거");

    cafeKiosk.removeAll();
    System.out.println("모두 제거");
    System.out.println(cafeKiosk.getBeaverages().size());
  }
}
