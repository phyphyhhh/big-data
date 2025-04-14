package com.example.demo;

public class Car extends Vehicle {
    private int numberOfDoors;

    public Car(String brand, String model, int year, int numberOfDoors) {
        super(brand, model, year);
        this.numberOfDoors = numberOfDoors;
    }

    @Override
    public void displayInfo() {
        System.out.println("Car - Brand: " + getBrand() + ", Model: " + getModel() + ", Year: " + getYear() + ", Number of Doors: " + numberOfDoors);
    }
}