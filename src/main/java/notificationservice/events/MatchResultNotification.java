package notificationservice.events;

import eventbus.Event;

public class MatchResultNotification implements Event {
    private final String message;

    public MatchResultNotification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getType() {
        return "MatchResultNotification";  // Nom unique pour cet événement
    }
}
