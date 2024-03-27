package ru.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        DeliveryCostCalculator calculator = new DeliveryCostCalculator();
        String c = calculator.calculateDeliveryCost(15,"",false,"");
        System.out.println("cal= "+ c);
        String c2 = calculator.calculateDeliveryCost(31, "large", true, "");
        System.out.println("cal2= "+ c2);
        String c3 = calculator.calculateDeliveryCost(31, "large", false, "high");
        System.out.println("cal3= "+ c3);
    }
}