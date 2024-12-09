package teamservice.events;

import eventbus.Event;

public class TeamCreated implements Event {
    private final String id;
    private final String name;

    public TeamCreated(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return "TeamCreated";
    }
}
