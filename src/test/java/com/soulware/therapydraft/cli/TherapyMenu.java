package com.soulware.therapydraft.cli;

import java.util.Scanner;

public class TherapyMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Therapy Draft CLI ===");
            System.out.println("1. Crear Assessment");
            System.out.println("2. Crear Therapy Plan");
            System.out.println("3. Crear Weekly Sessions");
            System.out.println("4. Salir");
            System.out.print("Opción: ");

            String option = scanner.nextLine();

            switch (option) {
                case "1" -> System.out.println(">> Aquí llamas a la lógica para crear Assessment");
                case "2" -> System.out.println(">> Aquí llamas a la lógica para crear Therapy Plan");
                case "3" -> System.out.println(">> Aquí llamas a la lógica para crear Weekly Sessions");
                case "4" -> {
                    System.out.println("Saliendo...");
                    running = false;
                }
                default -> System.out.println("Opción inválida, intenta de nuevo.");
            }
        }
        scanner.close();
    }
}
