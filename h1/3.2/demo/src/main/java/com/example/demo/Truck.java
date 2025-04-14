package com.example.demo;

public class Truck extends Vehicle {
    private double loadCapacity;

    public Truck(String brand, String model, int year, double loadCapacity) {
        super(brand, model, year);
        this.loadCapacity = loadCapacity;
    }

    @Override
    public void displayInfo() {
        System.out.println("Truck - Brand: " + getBrand() + ", Model: " + getModel() + ", Year: " + getYear() + ", Load Capacity: " + loadCapacity + " tons");
    }
}