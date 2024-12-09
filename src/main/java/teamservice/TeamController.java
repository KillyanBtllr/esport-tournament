package teamservice;

import eventbus.EventBus;

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
            }
        }
    }

    private void addTeam() {
        System.out.print("Enter Team ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Team Name: ");
        String name = scanner.nextLine().trim();

        if (id.isEmpty() || name.isEmpty()) {
            System.out.println("Team ID and Name cannot be empty.");
            return;
        }

        if (teamService.addTeam(id, name)) {
            System.out.println("Team added successfully.");
        } else {
            System.out.println("A team with the same ID already exists.");
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

        if (teamService.removeTeam(id)) {
            System.out.println("Team removed successfully.");
        } else {
            System.out.println("Team not found.");
        }
    }

    private void listAllTeams() {
        System.out.println("\n=== All Teams ===");
        teamService.getAllTeams().forEach((id, team) ->
                System.out.println("ID: " + id + " | Name: " + team.getName()));
    }
}
