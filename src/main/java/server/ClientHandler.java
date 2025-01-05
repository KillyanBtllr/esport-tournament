package server;

import eventbus.EventBus;
import eventbus.Event;
import teamservice.events.TeamCreated;
import teamservice.events.TeamDeleted;
import teamservice.events.TeamUpdated;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final EventBus eventBus;

    public ClientHandler(Socket socket, EventBus eventBus) {
        this.socket = socket;
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        try (InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream()) {

            Scanner scanner = new Scanner(input);
            PrintWriter writer = new PrintWriter(output, true);

            writer.println("Welcome to the Tournament Server!");
            String clientMessage;

            while ((clientMessage = scanner.nextLine()) != null) {
                System.out.println("Received from client: " + clientMessage);

                // Gestion des événements
                if (clientMessage.startsWith("event:")) {
                    String eventPayload = clientMessage.substring(6); // Supprimer "event:"
                    Event receivedEvent = parseEvent(eventPayload);

                    if (receivedEvent != null) {
                        // Publier l'événement localement
                        eventBus.publish(receivedEvent);

                        // Relayer l'événement à tous les clients
                        relayEventToAllClients(eventPayload);
                    }
                }

                // Répondre au client
                writer.println("Server received: " + clientMessage);
            }
        } catch (IOException ex) {
            System.out.println("Error handling client: " + ex.getMessage());
        }
    }

    private void relayEventToAllClients(String eventPayload) {
        // Envoyer l'événement à tous les clients connectés
        synchronized (Server.clientHandlers) {
            for (ClientHandler handler : Server.clientHandlers) {
                if (handler != this) { // Éviter d'envoyer à l'émetteur
                    handler.sendMessage("event:" + eventPayload);
                }
            }
        }
    }

    public void sendMessage(String message) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
        } catch (IOException e) {
            System.out.println("Error sending message to client: " + e.getMessage());
        }
    }


    private Event parseEvent(String payload) {
        try {
            String[] parts = payload.split(";");
            switch (parts[0]) {
                case "TeamCreated":
                    return new TeamCreated(parts[1], parts[2]); // ID, Name
                case "TeamUpdated":
                    return new TeamUpdated(parts[1], parts[2]); // ID, NewName
                case "TeamDeleted":
                    return new TeamDeleted(parts[1]); // ID
                default:
                    throw new IllegalArgumentException("Unknown event type: " + parts[0]);
            }
        } catch (Exception e) {
            System.out.println("Error parsing event: " + e.getMessage());
            return null;
        }
    }


}