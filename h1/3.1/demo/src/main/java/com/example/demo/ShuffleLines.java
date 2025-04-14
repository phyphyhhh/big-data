package com.example.demo;

import java.io.*;
import java.util.*;

public class ShuffleLines {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Not enough arguments.");
            return;
        }

        String inputFile = args[0];
        String outputFile = args[1];

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        Collections.shuffle(lines);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String line : lines) {
                String[] fields = line.split(",");
                String outputLine = fields[1] + "," + fields[0] + "," + fields[2];
                writer.write(outputLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
