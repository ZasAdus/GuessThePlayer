package org.example;
import java.sql.*;

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
            nationality VARCHAR(50) PRIMARY KEY,
            flagURL VARCHAR(255) NOT NULL
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
            firstName VARCHAR(50) NOT NULL,
            lastName VARCHAR(50) NOT NULL,
            clubID INT NOT NULL,
            nationality VARCHAR(50) NOT NULL,
            position VARCHAR(30) NOT NULL,
            photoURL VARCHAR(255) NOT NULL,
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

    public static void insertPlayerData(Integer id, String firstName, String lastName, Integer clubID, Integer nationalityID, String position, String photoURL){
        String sql = "INSERT INTO PLAYER(playerID, firstName, lastName, clubID, nationalityID, position, photoURL) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setInt(4, clubID);
            pstmt.setInt(5, nationalityID);
            pstmt.setString(6, position);
            pstmt.setString(7, photoURL);

            pstmt.executeUpdate();
            System.out.println("Player inserted successfully.");
        }catch(SQLException e){
            System.err.println("Error inserting player: " + e.getMessage());
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

    public static void insertNationalityData(Integer id, String name, String logoURL){
        String sql = "INSERT INTO NATIONALITY VALUES(?, ?, ?)";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, logoURL);
            pstmt.executeUpdate();
            System.out.println("Nationality inserted successfully.");
        }catch(SQLException e){
            System.err.println("Error inserting nationality: " + e.getMessage());
        }
    }
    public static boolean containsNationality(Integer id){
        String sql = "SELECT 1 FROM NATIONALITY WHERE nationalityID = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setInt(1, id);
            try(ResultSet rs = pstmt.executeQuery()){
                return rs.next();
            }
        }catch(SQLException e){
            System.err.println("Error checking for nationality: " + e.getMessage());
        }
        return false;
    }

}