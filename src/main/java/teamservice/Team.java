package teamservice;

import java.util.Objects;

public class Team {
    private final String id;
    private String name;
    private int points;
    private int win;

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
        this.points = 0;
        this.win = 0;
    }

    public Team(String id, String name, int points, int win) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.win = win;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public int getWins() {
        return win;
    }

    public int getPoints() {
        return points;
    }
}