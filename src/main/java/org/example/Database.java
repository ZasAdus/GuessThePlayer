package org.example;
import java.sql.*;
import java.util.ArrayList;

public class Database{
    private static Connection connection;

    public static void connect(){
        String url = "jdbc:mysql://localhost:3306/GuessThePlayer";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, System.getenv("userDB"), System.getenv("passwordDB"));
            System.out.println("Connection established successfully!");
        }catch(ClassNotFoundException e){
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }catch(SQLException e){
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public static void disconnect(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("Connection closed successfully!");
            }
        }catch(SQLException e){
            System.out.println("Error closing connection!");
            e.printStackTrace();
        }
    }

    private static void createTables(){
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
        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate(createLeagueTable);
            stmt.executeUpdate(createNationalityTable);
            stmt.executeUpdate(createClubTable);
            stmt.executeUpdate(createPlayerTable);
            System.out.println("Tables created successfully.");
        }catch(SQLException e){
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

    public static void insertLeagueData(Integer id, String name, String logoURL){
        String sql = "INSERT INTO LEAGUE VALUES(?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, logoURL);
            pstmt.executeUpdate();
            System.out.println("League inserted successfully.");
        }catch(SQLException e){
            System.err.println("Error inserting league: " + e.getMessage());
        }
    }

    public static void insertClubData(Integer id, String name, String logoURL, Integer leagueID){
        String sql = "INSERT INTO CLUB VALUES(?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, logoURL);
            pstmt.setInt(4, leagueID);
            pstmt.executeUpdate();
            System.out.println("Club inserted successfully.");
        }catch(SQLException e){
            System.err.println("Error inserting club: " + e.getMessage());
        }
    }

    public static void insertNationalityData(String name, String logoURL){
        String sql = "INSERT INTO NATIONALITY VALUES(?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, name);
            pstmt.setString(2, logoURL);
            pstmt.executeUpdate();
            System.out.println("Nationality inserted successfully.");
        }catch(SQLException e){
            System.err.println("Error inserting nationality: " + e.getMessage());
        }
    }
    public static boolean containsNationality(String nationality){
        String sql = "SELECT 1 FROM NATIONALITY WHERE name = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, nationality);
            try(ResultSet rs = pstmt.executeQuery()){
                return rs.next();
            }
        }catch(SQLException e){
            System.err.println("Error checking for nationality: " + e.getMessage());
        }
        return false;
    }

    public static ArrayList<Integer> getClubIDs(){
        String sql = "SELECT clubID FROM CLUB";
        ArrayList<Integer> clubIDs = new ArrayList<>();
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    clubIDs.add(rs.getInt("clubID"));
                }
            }
        }catch(SQLException e){
            System.err.println("Error checking for nationality: " + e.getMessage());
        }
        return clubIDs;
    }

    public static ArrayList<Integer> getPlayerIDs(){
        String sql = "SELECT playerID FROM PLAYER";
        ArrayList<Integer> playerIDs = new ArrayList<>();
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    playerIDs.add(rs.getInt("playerID"));
                }
            }
        }catch(SQLException e){
            System.err.println("Error checking for nationality: " + e.getMessage());
        }
        return playerIDs;
    }
}