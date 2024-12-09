package client;

import eventbus.EventBus;

public class Main {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus(); // Initialisation du bus d'événements
        TournamentController controller = new TournamentController(eventBus);
        View view = new View(controller);

        view.show(); // Lancer l'interface utilisateur
    }
}
