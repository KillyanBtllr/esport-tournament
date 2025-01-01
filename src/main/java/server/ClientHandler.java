package server;

import eventbus.EventBus;
import teamservice.events.TeamCreated;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final EventBus eventBus;

    public ClientHandler(Socket clientSocket, EventBus eventBus) {
        this.clientSocket = clientSocket;
        this.eventBus = eventBus;

        // Listen to events on the EventBus
        this.eventBus.subscribe("TeamCreated", event -> {
            if (event instanceof TeamCreated teamCreated) { // Utilisation du pattern matching
                sendMessage("TeamCreated: " + teamCreated.getId());
            }
        });
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received from client: " + message);

                // Example: parse and publish events
                if (message.startsWith("CreateTeam")) {
                    String[] parts = message.split(";", 3);
                    if (parts.length == 3) {
                        String id = parts[1];
                        String name = parts[2];
                        eventBus.publish(new TeamCreated(id, name));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Client handler error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private void sendMessage(String message) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(message);
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}