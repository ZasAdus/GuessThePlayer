package org.example;
import org.json.*;

public class Player {
    private final int playerId;
    private String data;
    private String firstName;
    private String lastName;
    private String position;
    private String club;
    private String country;

    public Player(int id) {
        this.playerId = id;
        this.data = "";
    }

    public void appendData(String newData) {
        this.data += newData;
    }

    public void parseJson() {
        try {
            JSONObject jsonResponse = new JSONObject(data);

            if (jsonResponse.has("response") && !jsonResponse.getJSONArray("response").isEmpty()) {
                JSONArray responseArray = jsonResponse.getJSONArray("response");
                JSONObject playerData = responseArray.getJSONObject(0).getJSONObject("player");

                this.firstName = playerData.getString("firstname");
                this.lastName = playerData.getString("lastname");

                if (playerData.has("position")) {
                    this.position = playerData.getString("position");
                }


                if (responseArray.getJSONObject(0).has("statistics")) {
                    JSONArray stats = responseArray.getJSONObject(0).getJSONArray("statistics");
                    if (!stats.isEmpty() && stats.getJSONObject(0).has("team")) {
                        JSONObject team = stats.getJSONObject(0).getJSONObject("team");
                        if (team.has("name")) {
                            this.club = team.getString("name");
                        }
                    }
                }

                if (playerData.has("nationality")) {
                    this.country = playerData.getString("nationality");
                } else if (playerData.has("country")) {
                    this.country = playerData.getString("country");
                }
            }
        } catch (JSONException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
    }

    public int getId() {
        return playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }

    public String getClub() {
        return club;
    }

    public String getCountry() {
        return country;
    }

    public void display() {
        System.out.println("Name: " + firstName + " " + lastName);
        if (position != null && !position.isEmpty()) {
            System.out.println("Position: " + position);
        }
        if (club != null && !club.isEmpty()) {
            System.out.println("Club: " + club);
        }
        if (country != null && !country.isEmpty()) {
            System.out.println("Country: " + country);
        }
        System.out.println("Player ID: " + playerId);
    }
}