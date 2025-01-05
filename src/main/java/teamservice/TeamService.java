package teamservice;

import eventbus.EventBus;
import teamservice.events.TeamCreated;
import teamservice.events.TeamDeleted;
import teamservice.events.TeamUpdated;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TeamService {
    private final EventBus eventBus;
    private final Map<String, Team> teams;

    public TeamService(EventBus eventBus) {
        this.eventBus = eventBus;
        this.teams = new HashMap<>();
    }

    public boolean addTeam(String id, String name) {
        if (teams.containsKey(id)) {
            return false;
        }

        Team team = new Team(id, name);
        teams.put(id, team);
        eventBus.publish(new TeamCreated(id, name));
        return true;
    }

    public boolean updateTeam(String id, String newName) {
        Team team = teams.get(id);
        if (team == null) {
            return false;
        }

        team.setName(newName);
        eventBus.publish(new TeamUpdated(id, newName));
        return true;
    }

    public boolean removeTeam(String id) {
        if (teams.containsKey(id)) {
            teams.remove(id);
            eventBus.publish(new TeamDeleted(id));
            return true;
        }
        return false;
    }

    public Map<String, Team> getAllTeams() {
        return Collections.unmodifiableMap(teams);
    }
}
