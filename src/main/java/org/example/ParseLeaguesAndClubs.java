package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static org.example.FetchData.fetch;

public class ParseLeaguesAndClubs{
    private static final HashMap<Integer, String> leaguesMapIDtoName = new HashMap<>();
    private static ArrayList<Integer> clubIDs = new ArrayList<>();
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

    private static void fetchPlayersFromClubs(ArrayList<Integer> clubIDs){
        for(Integer clubID : clubIDs){
            System.out.println("Fetching players for club ID: " + clubID);
            data = fetch("https://v3.football.api-sports.io/players/squads?team=" + clubID);
            parsePlayersData(clubID);
            data = "";
        }
    }

    private static void parsePlayersData(int clubId) {
        try{
            JSONObject jsonResponse = new JSONObject(data);
            JSONArray responseArray = jsonResponse.getJSONArray("response");
            JSONObject responseItem = responseArray.getJSONObject(0);
            JSONArray players = responseItem.getJSONArray("players");
            for(int j = 0; j < players.length(); j++){
                JSONObject player = players.getJSONObject(j);
                Database.insertBasicPlayerData(player.getInt("id"),
                        clubId, player.getInt("age"),
                        player.getString("position"),
                        player.getInt("number"),
                        player.getString("photo")
                );
            }
        }catch(Exception e){
            System.err.println("Error parsing players data JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Database.connect();

        // Uncomment these lines for initial setup:
        // populateLeaguesMap();
        // fetchLeagueData();
        // fetchClubsData();

        clubIDs = Database.getClubIDs();
        fetchPlayersFromClubs(clubIDs);

        Database.disconnect();
    }
}