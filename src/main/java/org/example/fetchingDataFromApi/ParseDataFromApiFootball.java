package org.example.fetchingDataFromApi;

import org.example.Database;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.example.Database.getPlayerIDsFromDatabaseThatAreNotFullyInserted;
import static org.example.fetchingDataFromApi.FetchDataFromApiFootball.fetch;

public class ParseDataFromApiFootball {
    private static final HashMap<Integer, String> leaguesMapIDtoName = new HashMap<>();
    private static ArrayList<Integer> clubIDs = new ArrayList<>();
    private static ArrayList<Integer> playersIDs = new ArrayList<>();
    private static String data;

    private static void populateLeaguesMap(){
        leaguesMapIDtoName.put(39, "Premier League");
        leaguesMapIDtoName.put(140, "La Liga");
        leaguesMapIDtoName.put(135, "Serie A");
        leaguesMapIDtoName.put(61, "Bundesliga");
        leaguesMapIDtoName.put(78, "Ligue 1");
    }

    private static void fetchLeagueData(){
        for(Integer id : leaguesMapIDtoName.keySet()){
            data = fetch("https://v3.football.api-sports.io/leagues?id=" + id);
            parseLeagueData();
            data = "";
        }
    }

    private static void parseLeagueData(){
        try{
            JSONObject jsonResponse = new JSONObject(data);
            JSONArray responseArray = jsonResponse.getJSONArray("response");
            JSONObject responseItem = responseArray.getJSONObject(0);
            JSONObject league = responseItem.getJSONObject("league");
            Database.insertLeagueData(league.getInt("id"),
                    league.getString("name"),
                    league.getString("logo")
            );
        }catch(Exception e){
            System.err.println("Error parsing league data JSON: " + e.getMessage());
        }
    }

    private static void fetchClubsData(){
        for(Integer id : leaguesMapIDtoName.keySet()){
            data = fetch("https://v3.football.api-sports.io/teams?league=" + id + "&season=2023");
            parseClubsData(id);
            data = "";
        }
    }

    private static void parseClubsData(int leagueId){
        try{
            JSONObject jsonResponse = new JSONObject(data);
            JSONArray responseArray = jsonResponse.getJSONArray("response");
            for(int i = 0; i < responseArray.length(); i++){
                JSONObject responseItem = responseArray.getJSONObject(i);
                JSONObject team = responseItem.getJSONObject("team");
                Database.insertClubData(team.getInt("id"),
                        team.getString("name"),
                        team.getString("logo"),
                        leagueId
                );
            }
        }catch(Exception e){
            System.err.println("Error parsing clubs data JSON: " + e.getMessage());
        }
    }

    private static void fetchPlayersFromClubs(List<Integer> clubIDs) throws InterruptedException {
        int n = 0;
        for (Integer clubID : clubIDs) {
            if(n == 10){
                n = 0;
                Thread.sleep(Duration.ofSeconds(65));
            }
            System.out.println("Fetching players for club ID: " + clubID);
            data = fetch("https://v3.football.api-sports.io/players/squads?team=" + clubID);
            n++;
            parsePlayersBasicData(clubID);
            data = "";
        }
    }

    private static void parsePlayersBasicData(int clubId) {
        try{
            JSONObject jsonResponse = new JSONObject(data);
            JSONArray responseArray = jsonResponse.getJSONArray("response");
            JSONObject responseItem = responseArray.getJSONObject(0);
            JSONArray players = responseItem.getJSONArray("players");
            for(int j = 0; j < players.length(); j++){
                JSONObject player = players.getJSONObject(j);
                int number = -1;
                if(!player.isNull("number")){
                    number = player.getInt("number");
                }
                int age = -1;
                if(!player.isNull("age")){
                    age = player.getInt("age");
                }
                Database.insertBasicPlayerData(
                        player.getInt("id"),
                        clubId,
                        age,
                        player.getString("position"),
                        number,
                        player.getString("photo")
                );
            }
        }catch(Exception e){
            System.err.println("Error parsing players data JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void fetchRestOfThePlayersData(List<Integer> playerIDs) throws InterruptedException {
        int n = 0;
        int count = 0;
        for(Integer playerID : playerIDs){
            if(count == 100){
                return;
            }
            if(n == 10){
                n = 0;
                Thread.sleep(Duration.ofSeconds(65));
            }
            System.out.println("Fetching personal information for playerID: " + playerID);
            data = fetch("https://v3.football.api-sports.io/players/profiles?player=" + playerID);
            count++;
            n++;
            parsePersonalData(playerID);
            data = "";
        }
    }

    public static void parsePersonalData(int playerID){
        try{
            JSONObject jsonResponse = new JSONObject(data);
            if(jsonResponse.has("response") && !jsonResponse.getJSONArray("response").isEmpty()){
                JSONArray responseArray = jsonResponse.getJSONArray("response");
                JSONObject playerData = responseArray.getJSONObject(0).getJSONObject("player");
                String nationality = playerData.getString("nationality");
                System.out.println(nationality);
                Database.insertRestOfThePlayerData(playerID, playerData.getString("firstname"), playerData.getString("lastname"), nationality);
                data = "";
            }
        }catch(JSONException e){
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
    }

    private static void fetchAllNationalities() {
        HttpURLConnection connection = null;
        try {
            URL apiUrl = new URL("https://restcountries.com/v3.1/all");
            int attempt = 1;
            final int maxAttempts = 10;
            boolean isResponse200 = false;
            int httpCode;
            while(attempt <= maxAttempts && !isResponse200) {
                connection =(HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                httpCode = connection.getResponseCode();
                if(httpCode == 200) {
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        StringBuilder response = new StringBuilder();
                        while((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        data = response.toString();
                        isResponse200 = true;
                    }
                }else if(httpCode == 429) {
                    System.err.println("Rate limited, retrying attempt: " + attempt + "/10");
                    attempt++;
                    try {
                        Thread.sleep(1000);
                    }catch(InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }else {
                    System.err.println("Error, HTTP code: " + httpCode);
                    break;
                }
                connection.disconnect();
            }
        }catch(IOException e) {
            System.err.println("Error: " + e.getMessage());
        }finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        parseNationalities();
        data = "";
    }

    private static void parseNationalities() {
        try {
            JSONArray countries = new JSONArray(data);
            for (int i = 0; i < countries.length(); i++) {
                JSONObject country = countries.getJSONObject(i);
                JSONObject name = country.getJSONObject("name");
                String commonName = name.getString("common");
                JSONObject flags = country.getJSONObject("flags");
                String flagPng = flags.getString("png");
                System.out.println("Country: " + commonName + ", Flag: " + flagPng);
                if(!Database.containsNationality(commonName)){
                    Database.insertNationalityData(commonName, flagPng);
                }
            }
        } catch (Exception e) {
            System.err.println("Parsing error: " + e.getMessage());
        }
    }

    public static ArrayList<Integer> getPlayersIDs() {
        return playersIDs;
    }

    public static void setPlayersIDs(ArrayList<Integer> playersIDs) {
        ParseDataFromApiFootball.playersIDs = playersIDs;
    }

    public static ArrayList<Integer> getClubIDs() {
        return clubIDs;
    }

    public static void setClubIDs(ArrayList<Integer> clubIDs) {
        ParseDataFromApiFootball.clubIDs = clubIDs;
    }

    public static void main(String[] args) throws InterruptedException {
//        if(args.length != 1){
//            System.err.println("one argument is valid");
//        }else{
//            Database.connect();
//            int n = Integer.parseInt(args[0]);
//            setPlayersIDs(Database.getPlayerIDs());
//            fetchRestOfThePlayersData(getPlayersIDs().subList(n, n+100));
//            Database.disconnect();
//        }
//        populateLeaguesMap();
//        fetchLeagueData();
//        fetchClubsData();
//        clubIDs = Database.getClubIDs();
//        fetchPlayersFromClubs(clubIDs);
//        fetchAllNationalities();
        Database.connect();
        playersIDs = getPlayerIDsFromDatabaseThatAreNotFullyInserted();
        System.out.println(playersIDs);
        fetchRestOfThePlayersData(playersIDs);
        Database.disconnect();
    }
}