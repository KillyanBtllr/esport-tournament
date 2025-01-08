package matchservice;

import db.DataBaseManager;
import eventbus.EventBus;
import teamservice.Team;

import java.sql.SQLException;
import java.util.List;
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
            System.out.println("5. Display podium");
            System.out.println("6. Exit");
            System.out.print("Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 -> scheduleMatch();
                    case 2 -> completeMatch();
                    case 3 -> cancelMatch();
                    case 4 -> displayScheduledMatches();
                    case 5 -> displayPodium();
                    case 6 -> {
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

            String team1;
            String team2;

            while (true) {
                System.out.print("Enter Team 1: ");
                team1 = scanner.nextLine().trim();
                if (DataBaseManager.isTeamNameExists(team1)) {
                    break;
                } else {
                    System.out.println("Team 1 does not exist. Please enter a valid team name.");
                }
            }

            while (true) {
                System.out.print("Enter Team 2: ");
                team2 = scanner.nextLine().trim();
                if (DataBaseManager.isTeamNameExists(team2)) {
                    break;
                } else {
                    System.out.println("Team 2 does not exist. Please enter a valid team name.");
                }
            }

            if (team1.equals(team2)) {
                System.out.println("A match cannot be scheduled between the same team. Please enter different teams.");
                return;
            }

            matchService.scheduleMatch(matchId, team1, team2);
            System.out.println("Match scheduled successfully: " + team1 + " vs " + team2);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values for Match ID.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while accessing the database.");
        }
    }

    private void completeMatch() {
        try {
            System.out.print("Enter Team 1: ");
            String team1 = scanner.nextLine().trim();
            System.out.print("Enter Team 2: ");
            String team2 = scanner.nextLine().trim();

            if (!DataBaseManager.isTeamNameExists(team1) || !DataBaseManager.isTeamNameExists(team2)) {
                System.out.println("One or both team names are invalid. Please try again.");
                return;
            }

            int team1Id = DataBaseManager.getTeamIdByName(team1);
            int team2Id = DataBaseManager.getTeamIdByName(team2);

            System.out.print("Enter " + team1 + " Score: ");
            int score1 = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter " + team2 + " Score: ");
            int score2 = Integer.parseInt(scanner.nextLine().trim());

            DataBaseManager.updateTeamPoints(team1Id, score1);
            DataBaseManager.updateTeamPoints(team2Id, score2);

            if (score1 > score2) {
                DataBaseManager.updateTeamWins(team1Id);
                System.out.println(team1 + " wins the match!");
            } else if (score2 > score1) {
                DataBaseManager.updateTeamWins(team2Id);
                System.out.println(team2 + " wins the match!");
            } else {
                System.out.println("The match is a draw.");
            }

            System.out.println("Match completed successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values for scores.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while accessing the database.");
        }
    }

    private void displayPodium() {
        System.out.println("\n=== Podium ===");
        try {
            List<Team> ranking = DataBaseManager.getTeamsRanking();
            if (ranking.isEmpty()) {
                System.out.println("No teams available.");
            } else {
                ranking.forEach(team ->
                        System.out.println("Name: " + team.getName() +
                                " | Wins: " + team.getWins() +
                                " | Points: " + team.getPoints())
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve the ranking.");
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
