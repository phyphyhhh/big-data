package com.example.demo;

public class Main {
    public static void main(String[] args) {
        Vehicle[] vehicles = {
            new Car("Toyota", "Corolla", 2020, 4),
            new Truck("Volvo", "FH16", 2018, 30.5),
            new Motorcycle("Harley-Davidson", "Street 750", 2021, false)
        };

        for (Vehicle vehicle : vehicles) {
            vehicle.displayInfo();
        }
    }
}
