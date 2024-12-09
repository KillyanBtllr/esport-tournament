package client;

public class View {
    private final TournamentController controller;

    public View(TournamentController controller) {
        this.controller = controller;
    }

    public void show() {
        controller.startTournamentManagement();
    }
}
