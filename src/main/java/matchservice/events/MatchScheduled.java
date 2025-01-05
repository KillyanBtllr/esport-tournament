package matchservice.events;

import eventbus.Event;
import matchservice.Match;

public class MatchScheduled implements Event {
    private final Match match;

    public MatchScheduled(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    @Override
    public String getType() {
        return "MatchScheduled";
    }
}
