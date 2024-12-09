package teamservice.events;

import eventbus.Event;

public class TeamDeleted implements Event {
    private final String id;

    public TeamDeleted(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return "TeamDeleted";
    }
}
