package notificationservice;

import java.util.Scanner;

public class NotificationController {
    private final NotificationService notificationService;
    private final Scanner scanner = new Scanner(System.in);

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void start() {
        System.out.println("\n=== Notification Service ===");
        System.out.println("Notifications will be displayed here as they arrive.");
        System.out.println("Press Enter to return to the main menu.");
        scanner.nextLine();
    }
}
