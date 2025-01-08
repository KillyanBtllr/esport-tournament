package teamservice;

import db.DataBaseManager;
import eventbus.EventBus;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class TeamController {
    private final TeamService teamService;
    private final Scanner scanner;

    public TeamController(EventBus eventBus) {
        this.teamService = new TeamService(eventBus);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n=== Team Management ===");
            System.out.println("1. Add Team");
            System.out.println("2. Update Team");
            System.out.println("3. Remove Team");
            System.out.println("4. List All Teams");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addTeam();
                    case 2 -> updateTeam();
                    case 3 -> removeTeam();
                    case 4 -> listAllTeams();
                    case 5 -> {
                        System.out.println("Exiting Team Management...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void addTeam() throws SQLException {
        String name;
        do {
            System.out.print("Enter Team Name: ");
            name = scanner.nextLine().trim();

            if (DataBaseManager.isTeamNameExists(name)) {
                System.out.println("A team with this name already exists. Please enter a different name.");
            } else {
                break;
            }
        } while (true);

        if (teamService.addTeam(name)) {
            System.out.println("Team added successfully.");
        } else {
            System.out.println("Failed to add the team.");
        }
    }

    private void updateTeam() {
        System.out.print("Enter Team ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter New Team Name: ");
        String newName = scanner.nextLine().trim();

        if (newName.isEmpty()) {
            System.out.println("Team name cannot be empty.");
            return;
        }

        if (teamService.updateTeam(id, newName)) {
            System.out.println("Team updated successfully.");
        } else {
            System.out.println("Team not found.");
        }
    }

    private void removeTeam() {
        System.out.print("Enter Team ID: ");
        String id = scanner.nextLine().trim();

        try {
            if (DataBaseManager.removeTeamFromDatabase(id)) {
                System.out.println("Team removed successfully.");
            } else {
                System.out.println("Team not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to remove the team from the database.");
        }
    }

    private void listAllTeams() {
        System.out.println("\n=== All Teams ===");
        try {
            List<Team> teams = DataBaseManager.getAllTeamsFromDatabase();
            teams.forEach(team ->
                    System.out.println("ID: " + team.getId() + " | Name: " + team.getName()));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve teams from the database.");
        }
    }
}
