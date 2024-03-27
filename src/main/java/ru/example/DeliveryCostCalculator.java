package ru.example;

public class DeliveryCostCalculator {
    public String calculateDeliveryCost(double distance, String size, boolean fragile, String workload){
        double cost = 0;

//расстояние до пункта назначения
        if (distance > 30) {
            cost += 300;
        } else if (distance > 10) {
            cost += 200;
        } else if (distance > 2) {
            cost += 100;
        } else {
            cost += 50;
        }
//габариты груза
        cost += size.equalsIgnoreCase("large") ? 200 : 100;
//хрупкость груза
        if (fragile) {
            cost += 300;
            if (distance > 30) {
                return "Хрупкие грузы нельзя возить на расстояние более 30 км";
            }
        }
//загруженность службы доставки
        switch (workload) {
            case "very_high" -> cost *= 1.6;
            case "high" -> cost *= 1.4;
            case "elevated" -> cost *= 1.2;
            default -> cost *= 1;
        }
        return String.valueOf(Math.max(cost, 400));
    }
}
