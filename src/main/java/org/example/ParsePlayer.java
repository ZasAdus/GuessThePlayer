package org.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.example.FetchData.fetch;

public class ParsePlayer{
    private static String data;

    public static void fetchPersonalData(Player player){
        data = fetch("https://v3.football.api-sports.io/players/profiles?player="+ player.getId());
        parsePersonalDataJson(player);
        data = "";
   }
    public static void parsePersonalDataJson(Player player){
        try{
            JSONObject jsonResponse = new JSONObject(data);
            if(jsonResponse.has("response") && !jsonResponse.getJSONArray("response").isEmpty()){
                JSONArray responseArray = jsonResponse.getJSONArray("response");
                JSONObject playerData = responseArray.getJSONObject(0).getJSONObject("player");
                if(playerData.has("firstname")){
                    player.setFirstName(playerData.getString("firstname"));
               }
                if(playerData.has("lastname")){
                    player.setLastName(playerData.getString("lastname"));
               }
                if(playerData.has("position")){
                    player.setPosition(playerData.getString("position"));
               }
                if(playerData.has("nationality")){
                    player.setNationality(playerData.getString("nationality"));
               }
                if(playerData.has("photo")){
                    player.setPhotoURL(playerData.getString("photo"));
               }
                data = "";
           }
      }catch(JSONException e){
            System.err.println("Error parsing JSON: " + e.getMessage());
       }
   }

    public static void fetchClubData(Player player){
        data = fetch("https://v3.football.api-sports.io/players/squads?player="+ player.getId());
        parseClubDataJson(player);
        data = "";
   }

    public static void parseClubDataJson(Player player){
        try{
            boolean foundClub = false;
            boolean foundCountry = false;
            JSONObject jsonResponse = new JSONObject(data);
            JSONArray responseArray = jsonResponse.getJSONArray("response");
            for(int i = 0; i < responseArray.length(); i++){
                JSONObject teamObject = responseArray.getJSONObject(i).getJSONObject("team");
                String teamName = teamObject.getString("name");
                int ID = teamObject.getInt("id");
                String nationality = player.getNationality();
                if(!(teamName.equals(nationality) && player.getClubID() != null)){
                    player.setClubID(ID);
                    foundClub = true;
               }else{
                    player.setNationalityID(ID);
                    foundCountry = true;
                    if(!Database.containsNationality(ID)){
                        Database.insertNationalityData(ID, teamName, teamObject.getString("logo"));
                   }
               }
                if(foundClub && foundCountry){
                    break;
               }
           }
      }catch(JSONException e){
            System.err.println("Error parsing club data JSON: " + e.getMessage());
            e.printStackTrace();
      }catch(Exception e){
            System.err.println("Unexpected error parsing club data JSON: " + e.getMessage());
            e.printStackTrace();
       }
   }

    public static Player fetchPlayer(int playerId){
        Player player = new Player(playerId);
        fetchPersonalData(player);
        fetchClubData(player);
        return player;
   }
}