package db;

import teamservice.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {

    private static final String DB_URL = "jdbc:sqlite:teams.db";

    public static void createDatabase() {
        try {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (conn != null) {
                    String createTableSQL = "CREATE TABLE IF NOT EXISTS Team (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "name TEXT NOT NULL, " +
                            "points INTEGER DEFAULT 0, " +
                            "win INTEGER DEFAULT 0)";
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute(createTableSQL);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addTeamInDatabase(String teamName) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO Team (name) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, teamName);
                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Successfully added team: " + teamName);
                } else {
                    System.out.println("Failed to add team: " + teamName);
                }
            }
        }
    }

    public static int getTeamIdByName(String teamName) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT id FROM Team WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, teamName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Team not found");
                }
            }
        }
    }

    public static void updateTeamPoints(int teamId, int points) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "UPDATE Team SET points = points + ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, points);
                pstmt.setInt(2, teamId);
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Updated points for team ID: " + teamId);
                } else {
                    System.out.println("No team found with ID: " + teamId);
                }
            }
        }
    }

    public static void updateTeamWins(int teamId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "UPDATE Team SET win = win + 1 WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, teamId);
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Updated win count for team ID: " + teamId);
                } else {
                    System.out.println("No team found with ID: " + teamId);
                }
            }
        }
    }

    public static boolean isTeamNameExists(String teamName) throws SQLException {
        boolean exists = false;
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT COUNT(*) AS count FROM Team WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, teamName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    exists = rs.getInt("count") > 0;
                }
            }
        }
        return exists;
    }

    public static List<Team> getAllTeamsFromDatabase() throws SQLException {
        List<Team> teams = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT id, name FROM Team";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Team team = new Team(rs.getString("id"), rs.getString("name"));
                    teams.add(team);
                }
            }
        }
        return teams;
    }

    public static boolean removeTeamFromDatabase(String teamId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM Team WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, teamId);
                int rowsDeleted = pstmt.executeUpdate();
                return rowsDeleted > 0;
            }
        }
    }

    public static List<Team> getTeamsRanking() throws SQLException {
        List<Team> ranking = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT id, name, win, points FROM Team ORDER BY points DESC, win DESC";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Team team = new Team(rs.getString("id"), rs.getString("name"), rs.getInt("points"), rs.getInt("win"));
                    ranking.add(team);
                }
            }
        }
        return ranking;
    }
}
