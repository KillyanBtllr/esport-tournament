package matchservice.events;

import eventbus.Event;
import matchservice.Match;

public class MatchCancelled implements Event {
    private final Match match;

    public MatchCancelled(Match match) {
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }

    @Override
    public String getType() {
        return "MatchCancelled";
    }

    @Override
    public String toString() {
        return "MatchCancelled{" +
                "match=" + match +
                '}';
    }
}
