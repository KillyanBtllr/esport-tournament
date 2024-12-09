package matchservice;

import java.util.Objects;

public class Match {
    private int matchId;
    private String team1;
    private String team2;
    private int scoreTeam1;
    private int scoreTeam2;
    private boolean isCompleted;
    private boolean isCancelled;

    // Constructeur avec 3 arguments
    public Match(int matchId, String team1, String team2) {
        this.matchId = matchId;
        this.team1 = team1;
        this.team2 = team2;
        this.scoreTeam1 = 0;  // Score initial de 0
        this.scoreTeam2 = 0;  // Score initial de 0
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

    public int getScoreTeam1() {
        return scoreTeam1;
    }

    public int getScoreTeam2() {
        return scoreTeam2;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    // Méthode pour marquer le match comme terminé et mettre à jour les scores
    public void completeMatch(int scoreTeam1, int scoreTeam2) {
        if (!isCancelled) {
            this.scoreTeam1 = scoreTeam1;
            this.scoreTeam2 = scoreTeam2;
            this.isCompleted = true;
        } else {
            System.out.println("Cannot complete a cancelled match.");
        }
    }

    // Nouvelle méthode pour mettre à jour les scores du match
    public void setScore(int scoreTeam1, int scoreTeam2) {
        if (isCancelled) {
            System.out.println("Cannot set scores for a cancelled match.");
        } else if (isCompleted) {
            System.out.println("Match is already completed, scores cannot be changed.");
        } else {
            this.scoreTeam1 = scoreTeam1;
            this.scoreTeam2 = scoreTeam2;
            System.out.println("Scores updated: " + team1 + " " + scoreTeam1 + " - " + scoreTeam2 + " " + team2);
        }
    }

    // Méthode pour annuler un match
    public void cancelMatch() {
        if (!isCompleted) {
            this.isCancelled = true;
            System.out.println("Match " + matchId + " has been cancelled.");
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
