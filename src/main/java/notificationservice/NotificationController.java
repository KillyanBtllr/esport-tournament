package notificationservice;

import eventbus.Event;
import eventbus.EventBus;

import java.util.Scanner;

public class NotificationController {
    private final NotificationService notificationService;
    private final EventBus eventBus;
    private final Scanner scanner = new Scanner(System.in);
    private boolean running = true;

    public NotificationController(NotificationService notificationService, EventBus eventBus) {
        this.notificationService = notificationService;
        this.eventBus = eventBus;
    }

    public void start() {
        System.out.println("\n=== Notification Service ===");
        System.out.println("Notifications will be displayed here as they arrive.");
        System.out.println("Type 'exit' and press Enter to return to the main menu.");

        // Lancement d'un thread pour écouter les événements en temps réel
        Thread notificationThread = new Thread(this::listenForNotifications);
        notificationThread.start();

        // Gérer l'entrée utilisateur
        while (running) {
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input.trim())) {
                running = false;
            }
        }

        System.out.println("Exiting Notification Service...");
        try {
            notificationThread.join(); // Assure que le thread se termine proprement
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error stopping notification thread: " + e.getMessage());
        }
    }

    private void listenForNotifications() {
        eventBus.subscribe("*", this::displayNotification); // Écouter tous les événements
    }

    private void displayNotification(Event event) {
        System.out.println("New Event Received: " + event.getType() + " - " + event.toString());
    }
}
