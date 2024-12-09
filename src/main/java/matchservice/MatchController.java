package matchservice;

import eventbus.EventBus;

import java.util.Scanner;

public class MatchController {
    private final MatchService matchService;
    private final Scanner scanner;

    public MatchController(EventBus eventBus) {
        this.matchService = new MatchService(eventBus);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Match Management ===");
            System.out.println("1. Schedule a Match");
            System.out.println("2. Complete a Match");
            System.out.println("3. Cancel a Match");
            System.out.println("4. Display Scheduled Matches");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> scheduleMatch();
                    case 2 -> completeMatch();
                    case 3 -> cancelMatch();
                    case 4 -> displayScheduledMatches();
                    case 5 -> {
                        System.out.println("Exiting Match Management...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void scheduleMatch() {
        try {
            System.out.print("Enter Match ID: ");
            int matchId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Team 1: ");
            String team1 = scanner.nextLine().trim();
            System.out.print("Enter Team 2: ");
            String team2 = scanner.nextLine().trim();
            if (team1.isEmpty() || team2.isEmpty()) {
                System.out.println("Team names cannot be empty.");
                return;
            }
            matchService.scheduleMatch(matchId, team1, team2);
            System.out.println("Match scheduled successfully: " + team1 + " vs " + team2);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values for Match ID.");
        }
    }

    private void completeMatch() {
        try {
            System.out.print("Enter Match ID: ");
            int matchId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Team 1 Score: ");
            int score1 = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Team 2 Score: ");
            int score2 = Integer.parseInt(scanner.nextLine().trim());
            matchService.completeMatch(matchId, score1, score2);
            System.out.println("Match completed successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values for Match ID and scores.");
        }
    }

    private void cancelMatch() {
        try {
            System.out.print("Enter Match ID: ");
            int matchId = Integer.parseInt(scanner.nextLine().trim());
            matchService.cancelMatch(matchId);
            System.out.println("Match cancelled successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a numeric Match ID.");
        }
    }

    private void displayScheduledMatches() {
        System.out.println("\n=== Scheduled Matches ===");
        var matches = matchService.getScheduledMatches();
        if (matches.isEmpty()) {
            System.out.println("No matches scheduled.");
        } else {
            matches.forEach(match ->
                    System.out.println("- Match ID: " + match.getMatchId() +
                            " | " + match.getTeam1() + " vs " + match.getTeam2())
            );
        }
    }
}
