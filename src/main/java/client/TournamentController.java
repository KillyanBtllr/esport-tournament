package client;

import eventbus.EventBus;
import eventbus.Event;
import matchservice.Match;
import matchservice.events.MatchScheduled;
import notificationservice.events.MatchResultNotification;
import matchservice.events.MatchCancelled;
import teamservice.events.TeamCreated;
import teamservice.events.TeamDeleted;
import teamservice.events.TeamUpdated;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class TournamentController {

    private final EventBus eventBus;
    private List<Match> matchesList = new ArrayList<>();  // Liste des matchs en cours
    private List<String> existingTeams = new ArrayList<>();  // Liste des équipes existantes
    private static int currentMatchId = 0;

    public TournamentController(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    // Méthode pour ajouter une équipe à la liste des équipes existantes
    private void addTeam(String teamName) {
        existingTeams.add(teamName);  // Ajoute l'équipe à la liste
    }

    public void startTournamentManagement() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Tournament Manager ===");
            System.out.println("1. Add a team");
            System.out.println("2. Schedule a match");
            System.out.println("3. End a match");
            System.out.println("4. Cancel a match");
            System.out.println("5. Display the leaderboard");
            System.out.println("6. Display existing teams");
            System.out.println("7. Display scheduled matches");
            System.out.println("8. Quit");
            System.out.print("Choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addTeam(scanner);
                    break;
                case 2:
                    scheduleMatch(scanner);
                    break;
                case 3:
                    endMatch(scanner);
                    break;
                case 4:
                    cancelMatch(scanner);
                    break;
                case 5:
                    displayLeaderboard();
                    break;
                case 6:
                    displayExistingTeams();
                    break;
                case 7:
                    displayScheduledMatches();
                    break;
                case 8:
                    System.out.println("Exiting Tournament Management...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void addTeam(Scanner scanner) {
        System.out.print("Enter team name: ");
        String teamName = scanner.next();
        addTeam(teamName);  // Ajoute l'équipe à la liste des équipes existantes
        String teamId = generateTeamId();
        TeamCreated event = new TeamCreated(teamName, teamId);
        eventBus.publish(event);
        System.out.println("Team added: " + teamName);
    }

    private String generateTeamId() {
        return "team-" + System.currentTimeMillis();
    }

    private void scheduleMatch(Scanner scanner) {
        System.out.print("Enter the name of the first team: ");
        String team1 = scanner.next();
        System.out.print("Enter the name of the second team: ");
        String team2 = scanner.next();

        // Vérification de l'existence des équipes
        if (!existingTeams.contains(team1)) {
            System.out.println("Error: Team " + team1 + " does not exist.");
            return;
        }
        if (!existingTeams.contains(team2)) {
            System.out.println("Error: Team " + team2 + " does not exist.");
            return;
        }

        // Créer un match sans score
        int matchId = currentMatchId++;
        Match match = new Match(matchId, team1, team2);
        matchesList.add(match);  // Ajouter le match à la liste
        MatchScheduled event = new MatchScheduled(match);
        eventBus.publish(event);

        System.out.println("Match scheduled between " + team1 + " and " + team2);
    }

    private void endMatch(Scanner scanner) {
        System.out.print("Enter the match ID to end: ");
        int matchId = scanner.nextInt();

        // Recherche du match par ID dans la liste
        Match match = findMatchById(matchId);

        if (match == null) {
            System.out.println("Error: Match with ID " + matchId + " does not exist.");
            return;
        }

        System.out.print("Enter the score of the first team: ");
        int score1 = scanner.nextInt();
        System.out.print("Enter the score of the second team: ");
        int score2 = scanner.nextInt();

        // Mettre à jour le match avec les scores
        match.setScore(score1, score2);

        // Publier l'événement de fin de match
        MatchResultNotification resultNotification = new MatchResultNotification(
                match.getTeam1() + " vs " + match.getTeam2() + " | Result: " + (score1 > score2 ? match.getTeam1() : match.getTeam2()) + " won!"
        );
        eventBus.publish(resultNotification);

        System.out.println("Match ended: " + match.getTeam1() + " vs " + match.getTeam2());
    }

    private void cancelMatch(Scanner scanner) {
        System.out.print("Enter the match ID to cancel: ");
        int matchId = scanner.nextInt();

        // Recherche du match par ID dans la liste
        Match match = findMatchById(matchId);

        if (match == null) {
            System.out.println("Error: Match with ID " + matchId + " does not exist.");
            return;
        }

        // Publier l'événement d'annulation du match
        MatchCancelled matchCancelled = new MatchCancelled(match);
        eventBus.publish(matchCancelled);

        System.out.println("Match with ID " + matchId + " has been cancelled.");
    }

    private Match findMatchById(int matchId) {
        for (Match match : matchesList) {
            if (match.getMatchId() == matchId) {
                return match;
            }
        }
        return null;
    }


    private void displayLeaderboard() {
        System.out.println("Displaying leaderboard...");
    }

    private void displayExistingTeams() {
        System.out.println("=== Existing Teams ===");
        if (existingTeams.isEmpty()) {
            System.out.println("No teams have been added yet.");
        } else {
            for (String team : existingTeams) {
                System.out.println("- " + team);
            }
        }
    }

    private void displayScheduledMatches() {
        System.out.println("=== Scheduled Matches ===");
        if (matchesList.isEmpty()) {
            System.out.println("No matches have been scheduled yet.");
        } else {
            for (Match match : matchesList) {
                System.out.println("- " + match.getMatchId() + ": " + match.getTeam1() + " vs " + match.getTeam2());
            }
        }
    }
}