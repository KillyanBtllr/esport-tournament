package server;

import eventbus.EventBus;
import teamservice.events.TeamCreated;
import teamservice.events.TeamUpdated;
import teamservice.events.TeamDeleted;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final int port;
    private final EventBus eventBus;
    public static final Set<ClientHandler> clientHandlers = ConcurrentHashMap.newKeySet();


    public Server(int port, EventBus eventBus) {
        this.port = port;
        this.eventBus = eventBus;

        // Abonnements aux événements
        registerEventListeners();
    }

    private void registerEventListeners() {
        eventBus.subscribe("TeamCreated", event -> {
            TeamCreated teamCreatedEvent = (TeamCreated) event;
            System.out.println("Event Received: Team Created - ID: " + teamCreatedEvent.getId() + ", Name: " + teamCreatedEvent.getName());
        });

        eventBus.subscribe("TeamUpdated", event -> {
            TeamUpdated teamUpdatedEvent = (TeamUpdated) event;
            System.out.println("Event Received: Team Updated - ID: " + teamUpdatedEvent.getId() + ", New Name: " + teamUpdatedEvent.getNewName());
        });

        eventBus.subscribe("TeamDeleted", event -> {
            TeamDeleted teamDeletedEvent = (TeamDeleted) event;
            System.out.println("Event Received: Team Deleted - ID: " + teamDeletedEvent.getId());
        });
    }

    private volatile boolean running = true;

    public void stop() {
        running = false;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (running) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, eventBus);
                Server.clientHandlers.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException ex) {
            System.out.println("Error starting the server: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        Server server = new Server(12345, eventBus);
        server.start();
    }
}