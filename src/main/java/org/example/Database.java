package org.example;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static Connection connection;

    public static void connect() {
        String url = "jdbc:mysql://localhost:3306/GuessThePlayer";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, System.getenv("userDB"), System.getenv("passwordDB"));
            System.out.println("Connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection!");
            e.printStackTrace();
        }
    }

    private static void createTables() {
        String createLeagueTable = """
        CREATE TABLE IF NOT EXISTS LEAGUE(
            leagueID INT PRIMARY KEY,
            name VARCHAR(50) NOT NULL,
            logoURL VARCHAR(255) NOT NULL
        );
    """;

        String createNationalityTable = """
        CREATE TABLE IF NOT EXISTS NATIONALITY(
            name VARCHAR(50) PRIMARY KEY,
            logoURL VARCHAR(255) NOT NULL
        );
    """;

        String createClubTable = """
        CREATE TABLE IF NOT EXISTS CLUB(
            clubID INT PRIMARY KEY,
            name VARCHAR(50) NOT NULL,
            logoURL VARCHAR(255) NOT NULL,
            leagueID INT NOT NULL,
            FOREIGN KEY(leagueID) REFERENCES LEAGUE(leagueID)
        );
    """;

        String createPlayerTable = """
        CREATE TABLE IF NOT EXISTS PLAYER(
            playerID INT PRIMARY KEY,
            firstName VARCHAR(50) NULL,
            lastName VARCHAR(50) NULL,
            clubID INT NOT NULL,
            nationality VARCHAR(50) NULL,
            position VARCHAR(30) NULL,
            photoURL VARCHAR(255) NULL,
            FOREIGN KEY(clubID) REFERENCES CLUB(clubID),
            FOREIGN KEY(nationality) REFERENCES NATIONALITY(nationality)
        );
    """;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createLeagueTable);
            stmt.executeUpdate(createNationalityTable);
            stmt.executeUpdate(createClubTable);
            stmt.executeUpdate(createPlayerTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public static void insertBasicPlayerData(int playerID, int clubID, int age, String position, int shirtNumber, String photoURL) {
        String sql = "INSERT IGNORE INTO PLAYER (playerID, clubID, age, position, shirtNumber, photoURL) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerID);
            pstmt.setInt(2, clubID);
            pstmt.setInt(3, age);
            pstmt.setString(4, position);
            pstmt.setInt(5, shirtNumber);
            pstmt.setString(6, photoURL);
            pstmt.executeUpdate();
            System.out.println("Inserted player ID: " + playerID + " for club ID: " + clubID);
        } catch (SQLException e) {
            System.err.println("Error inserting basic player data: " + e.getMessage());
        }
    }

    public static void insertRestOfThePlayerData(int playerID, String firstName, String lastName, String nationality) {
        String sql = "UPDATE PLAYER SET firstName = ?, lastName = ?, nationality = ? WHERE playerID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, nationality);
            pstmt.setInt(4, playerID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Updated player data for ID: " + playerID);
            }
        } catch (SQLException e) {
            System.err.println("Error updating player data: " + e.getMessage());
        }
    }

    public static void insertLeagueData(Integer id, String name, String logoURL) {
        String sql = "INSERT INTO LEAGUE VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, logoURL);
            pstmt.executeUpdate();
            System.out.println("League inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting league: " + e.getMessage());
        }
    }

    public static void insertClubData(Integer id, String name, String logoURL, Integer leagueID) {
        String sql = "INSERT INTO CLUB VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, logoURL);
            pstmt.setInt(4, leagueID);
            pstmt.executeUpdate();
            System.out.println("Club inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting club: " + e.getMessage());
        }
    }

    public static void insertNationalityData(String name, String logoURL) {
        String sql = "INSERT INTO NATIONALITY VALUES(?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, logoURL);
            pstmt.executeUpdate();
            System.out.println("Nationality inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting nationality: " + e.getMessage());
        }
    }

    public static boolean containsNationality(String nationality) {
        String sql = "SELECT 1 FROM NATIONALITY WHERE name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nationality);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking for nationality: " + e.getMessage());
        }
        return false;
    }

    public static ArrayList<Integer> getClubIDs() {
        String sql = "SELECT clubID FROM CLUB";
        ArrayList<Integer> clubIDs = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clubIDs.add(rs.getInt("clubID"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error looking for clubIDs: " + e.getMessage());
        }
        return clubIDs;
    }

    public static ArrayList<Integer> getPlayerIDsFromDatabaseThatAreNotFullyInserted() {
        String sql = "SELECT playerID FROM PLAYER WHERE firstName IS NULL";
        ArrayList<Integer> playerIDs = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    playerIDs.add(rs.getInt("playerID"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error looking for playerIDs: " + e.getMessage());
        }
        return playerIDs;
    }

    public static ArrayList<Integer> getPlayerIDsFromDatabase() {
        String sql = "SELECT playerID FROM PLAYER";
        ArrayList<Integer> playerIDs = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    playerIDs.add(rs.getInt("playerID"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error looking for playerIDs: " + e.getMessage());
        }
        return playerIDs;
    }

    public static ArrayList<Player> getPlayersFromDatabase() {
        String sql = "SELECT * FROM PLAYER";
        ArrayList<Player> players = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Player player = new Player(rs.getInt("playerID"));
                    player.setClub(Database.getPlayerClubDataFromDatabase(rs.getInt("clubID")));
                    player.setFirstName(rs.getString("firstName"));
                    player.setLastName(rs.getString("lastName"));
                    player.setPhotoURL(rs.getString("photoURL"));
                    player.setNationality(Database.getPlayerNationalityDataFromDatabase(rs.getString("nationality")));
                    player.setAge(rs.getInt("age"));
                    player.setShirtNumber(rs.getInt("shirtNumber"));
                    player.setPosition(rs.getString("position"));
                    players.add(player);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error looking for playerIDs: " + e.getMessage());
        }
        return players;
    }

    public static Player getPlayerFromDatabase(Integer id) {
        String sql = "SELECT * FROM PLAYER WHERE id = ?";
        Player player = new Player(id);
        String nationality;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    player.setFirstName(rs.getString("firstName"));
                    player.setLastName(rs.getString("lastName"));
                    player.setPhotoURL(rs.getString("photoURL"));
                    player.setAge(rs.getInt("age"));
                    player.setShirtNumber(rs.getInt("shirtNumber"));
                    player.setPosition(rs.getString("position"));
                    player.setClub(Database.getPlayerClubDataFromDatabase(rs.getInt("clubID")));
                    nationality = rs.getString("nationality");
                    player.setNationality(Database.getPlayerNationalityDataFromDatabase(nationality));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error looking for playerID: " + e.getMessage());
        }
        return player;
    }

    private static Nationality getPlayerNationalityDataFromDatabase(String name) {
        String sql = "SELECT * FROM NATIONALITY WHERE name = ?";
        Nationality nationality = new Nationality(name);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                   nationality.setLogoURL(rs.getString("logoURL"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nationality;
    }

    public static Club getPlayerClubDataFromDatabase(int clubID) {
        String sql = "SELECT * FROM CLUB WHERE clubID = ?";
        Club club = new Club(clubID);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, clubID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    club.setName(rs.getString("name"));
                    club.setLogoURL(rs.getString("logoURL"));
                    club.setLeague(Database.getLeagueDataFromDatabase(rs.getInt("leagueID")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return club;
    }

    private static League getLeagueDataFromDatabase(int leagueID) {
        String sql = "SELECT * FROM LEAGUE WHERE leagueID = ?";
        League league = new League(leagueID);
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, leagueID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    league.setLeagueName(rs.getString("name"));
                    league.setLogoURL(rs.getString("logoURL"));
                }
                return league;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getNumberOfPlayers() {
        String sql = "SELECT COUNT(*) as numberOfPlayers FROM PLAYER";
        int number;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                number = rs.getInt("numberOfPlayers");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return number;
    }

    public static Player getPlayerFromDatabaseFullName(String fullName) {
        StringBuilder firstName = new StringBuilder(fullName.substring(0, fullName.indexOf("_"))).append("%");
        String sql = "SELECT * FROM PLAYER WHERE firstName like ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, String.valueOf(firstName));
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    StringBuilder name = new StringBuilder(rs.getString("firstName") + " " + rs.getString("lastName"));
                    if(String.valueOf(name).equals(fullName)){
                        return Database.getPlayerFromDatabase(rs.getInt("id"));
                    }
                }
            }
        }catch(SQLException e){
            System.err.println("Error looking for player: " + e.getMessage());
        }
        return new Player(1);
    }
}
