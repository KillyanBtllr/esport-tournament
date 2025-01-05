package matchservice.events;

import eventbus.Event;
import matchservice.Match;

public class MatchCompleted implements Event {
    private final Match match;

    public MatchCompleted(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    @Override
    public String getType() {
        return "MatchCompleted";
    }
}
