package notificationservice.events;

import eventbus.Event;

public class TeamCreatedNotification implements Event {
    private final String message;

    public TeamCreatedNotification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getType() {
        return "TeamCreatedNotification";
    }
}
