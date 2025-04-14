package com.example.demo;

public class Motorcycle extends Vehicle {
    private boolean hasSidecar;

    public Motorcycle(String brand, String model, int year, boolean hasSidecar) {
        super(brand, model, year);
        this.hasSidecar = hasSidecar;
    }

    @Override
    public void displayInfo() {
        System.out.println("Motorcycle - Brand: " + getBrand() + ", Model: " + getModel() + ", Year: " + getYear() + ", Has Sidecar: " + hasSidecar);
    }
}
