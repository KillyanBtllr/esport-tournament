package matchservice;

import eventbus.EventBus;
import matchservice.events.MatchCompleted;
import matchservice.events.MatchScheduled;
import matchservice.events.MatchCancelled;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MatchService {
    private final EventBus eventBus;
    private final Map<Integer, Match> matches;

    public MatchService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.matches = new HashMap<>();
    }

    public void scheduleMatch(int matchId, String team1, String team2) {
        if (matches.containsKey(matchId)) {
            System.out.println("A match with ID " + matchId + " already exists.");
            return;
        }
        Match match = new Match(matchId, team1, team2);
        matches.put(matchId, match);
        eventBus.publish(new MatchScheduled(match));
        System.out.println("Match scheduled: " + match);
    }

    public void completeMatch(int matchId, int scoreTeam1, int scoreTeam2) {
        Match match = matches.get(matchId);
        if (match == null) {
            System.out.println("Match with ID " + matchId + " not found.");
            return;
        }
        if (match.isCompleted()) {
            System.out.println("Match with ID " + matchId + " is already completed.");
            return;
        }
        match.completeMatch(scoreTeam1, scoreTeam2);
        eventBus.publish(new MatchCompleted(match));
        System.out.println("Match completed: " + match);
    }

    public void cancelMatch(int matchId) {
        Match match = matches.get(matchId);
        if (match == null) {
            System.out.println("Match with ID " + matchId + " not found.");
            return;
        }
        if (match.isCompleted()) {
            System.out.println("Match with ID " + matchId + " is already completed and cannot be cancelled.");
            return;
        }
        matches.remove(matchId);
        eventBus.publish(new MatchCancelled(match));
        System.out.println("Match cancelled: " + match);
    }

    public Match getMatch(int matchId) {
        return matches.get(matchId);
    }

    public Collection<Match> getScheduledMatches() {
        return matches.values();
    }
}
