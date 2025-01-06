package client;

import eventbus.EventBus;
import notificationservice.NotificationService;
import teamservice.TeamController;
import matchservice.MatchController;
import notificationservice.NotificationController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    private static Socket socket;
    private static PrintWriter writer;
    private static Scanner serverScanner;

    public static void main(String[] args) {
        String serverHost = "127.0.0.1";
        int serverPort = 12345;

        try {
            socket = new Socket(serverHost, serverPort);
            writer = new PrintWriter(socket.getOutputStream(), true);
            serverScanner = new Scanner(socket.getInputStream());
            System.out.println("Connected to the server");

            writer.println("Main Client Connected.");

            // Initialisation du bus d'événements
            EventBus eventBus = new EventBus();

            // Instanciation des différents contrôleurs
            TeamController teamController = new TeamController(eventBus);
            MatchController matchController = new MatchController(eventBus);
            NotificationController notificationController =
                    new NotificationController(new NotificationService(eventBus), eventBus);

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
                        teamController.start();
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
                        closeConnection();
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException ex) {
            System.out.println("Error connecting to the server: " + ex.getMessage());
        }
    }

    private static void closeConnection() {
        try {
            if (writer != null) writer.close();
            if (serverScanner != null) serverScanner.close();
            if (socket != null) socket.close();
            System.out.println("Connection to the server closed.");
        } catch (IOException ex) {
            System.out.println("Error closing connection: " + ex.getMessage());
        }
    }
}
