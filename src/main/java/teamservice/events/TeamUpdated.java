package teamservice.events;

import eventbus.Event;

public class TeamUpdated implements Event {
    private final String id;
    private final String newName;

    public TeamUpdated(String id, String newName) {
        this.id = id;
        this.newName = newName;
    }

    public String getId() {
        return id;
    }

    public String getNewName() {
        return newName;
    }

    @Override
    public String getType() {
        return "TeamUpdated";
    }
}
