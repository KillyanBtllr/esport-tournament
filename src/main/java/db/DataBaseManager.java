package db;

import java.sql.*;

public class DataBaseManager {

    private static final String DB_URL = "jdbc:sqlite:teams.db";

    public static void createDatabase() {
        try {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (conn != null) {
                    // Créer la table si elle n'existe pas déjà
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

    public static int getTeamIdByName(String teamName) throws SQLException {
        int teamId = -1; // Valeur par défaut si l'équipe n'est pas trouvée
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT id FROM Team WHERE name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, teamName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    teamId = rs.getInt("id");
                }
            }
        }
        return teamId;
    }

    // Méthode pour mettre à jour les points des équipes
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
}
