package matchservice;

import db.DataBaseManager;

import java.util.Objects;
import java.sql.SQLException;

public class Match {
    private int matchId;
    private String team1;
    private String team2;
    private int scoreTeam1;
    private int scoreTeam2;
    private boolean isCompleted;
    private boolean isCancelled;

    public Match(int matchId, String team1, String team2) {
        this.matchId = matchId;
        this.team1 = team1;
        this.team2 = team2;
        this.scoreTeam1 = 0;
        this.scoreTeam2 = 0;
        this.isCompleted = false;
        this.isCancelled = false;
    }

    public int getMatchId() {
        return matchId;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void completeMatch(int scoreTeam1, int scoreTeam2) {

        if (!isCancelled) {
            this.scoreTeam1 = scoreTeam1;
            this.scoreTeam2 = scoreTeam2;
            this.isCompleted = true;
            System.out.println("Match comdfdsfdsfsddsfpleted: ");
            updateDatabase();

            System.out.println("Match completed: " + team1 + " vs " + team2);
        } else {
            System.out.println("Cannot complete a cancelled match.");
        }
    }


    private void updateDatabase() {
        try {
            int team1Id = DataBaseManager.getTeamIdByName(team1);
            int team2Id = DataBaseManager.getTeamIdByName(team2);

            DataBaseManager.updateTeamPoints(team1Id, scoreTeam1);
            DataBaseManager.updateTeamPoints(team2Id, scoreTeam2);

            if (scoreTeam1 > scoreTeam2) {
                DataBaseManager.updateTeamWins(team1Id);
            } else if (scoreTeam2 > scoreTeam1) {
                DataBaseManager.updateTeamWins(team2Id);
            } else {
                System.out.println("It's a draw!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelMatch(int matchId) {
        if (!isCompleted) {
            this.isCancelled = true;
        } else {
            System.out.println("Cannot cancel a completed match.");
        }
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId='" + matchId + '\'' +
                ", team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", scoreTeam1=" + scoreTeam1 +
                ", scoreTeam2=" + scoreTeam2 +
                ", isCompleted=" + isCompleted +
                ", isCancelled=" + isCancelled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match match)) return false;
        return Objects.equals(matchId, match.matchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId);
    }
}
