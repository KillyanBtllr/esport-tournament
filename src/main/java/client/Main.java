package client;

import eventbus.EventBus;
import notificationservice.NotificationService;
import teamservice.TeamController;
import matchservice.MatchController;
import notificationservice.NotificationController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialisation du bus d'événements
        EventBus eventBus = new EventBus();

        // Instanciation des différents contrôleurs
        TeamController teamController = new TeamController(eventBus);
        MatchController matchController = new MatchController(eventBus);
        NotificationController notificationController = new NotificationController(new NotificationService(eventBus));

        // Scanner pour les interactions utilisateur
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Team Management");
            System.out.println("2. Match Management");
            System.out.println("3. Notification Management");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> {
                    System.out.println("Launching Team Management...");
                    teamController.start(); // Lance le menu du TeamController
                }
                case 2 -> {
                    System.out.println("Launching Match Management...");
                    matchController.start();
                }
                case 3 -> {
                    System.out.println("Launching Notification Management...");
                    notificationController.start();
                }
                case 4 -> {
                    System.out.println("Exiting the application. Goodbye!");
                    return; // Quitte l'application
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}