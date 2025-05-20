package org.example;
import org.json.*;

public class Player{
    private final int playerId;
    private String data;
    private String firstName;
    private String lastName;
    private String position;
    private String club;
    private String nationality;

    public Player(int id){
        this.playerId = id;
        this.data = "";
    }

    public void appendData(String newData){
        this.data += newData;
    }

    public void parsePersonalDataJson(){
        try{
            JSONObject jsonResponse = new JSONObject(data);
            if(jsonResponse.has("response") && !jsonResponse.getJSONArray("response").isEmpty()){
                JSONArray responseArray = jsonResponse.getJSONArray("response");
                JSONObject playerData = responseArray.getJSONObject(0).getJSONObject("player");
                if(playerData.has("firstname")){
                    this.firstName = playerData.getString("firstname");
                }
                if(playerData.has("lastname")){
                    this.lastName = playerData.getString("lastname");
                }
                if(playerData.has("position")){
                    this.position = playerData.getString("position");
                }
                if(playerData.has("position")){
                    this.position = playerData.getString("position");
                }
                if(playerData.has("nationality")){
                    this.nationality = playerData.getString("nationality");
                }
                this.data = "";
            }
        } catch(JSONException e){
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
    }

    public void parseClubDataJson(){
        try{
            JSONObject jsonResponse = new JSONObject(data);
            JSONArray responseArray = jsonResponse.getJSONArray("response");
            for(int i = 0; i < 2; i++){
                JSONObject teamData = responseArray.getJSONObject(i);
                JSONObject team = teamData.getJSONObject("team");
                String teamName = team.getString("name");
                if(!teamName.equalsIgnoreCase(this.getNationality())){
                    this.club = teamName;
                    break;
                }
            }
            data = "";
        } catch(Exception e){
            System.err.println("Error parsing club data JSON: " + e.getMessage());
        }
    }

    public int getId(){
        return playerId;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPosition(){
        return position;
    }

    public String getClub(){
        return club;
    }

    public String getNationality(){
        return nationality;
    }

    public void display(){
        System.out.println("Name: " + firstName + " " + lastName);
        if(position != null && !position.isEmpty()){
            System.out.println("Position: " + position);
        }
        if(club != null && !club.isEmpty()){
            System.out.println("Club: " + club);
        }
        if(nationality != null && !nationality.isEmpty()){
            System.out.println("Country: " + nationality);
        }
        System.out.println("Player ID: " + playerId);
    }

}