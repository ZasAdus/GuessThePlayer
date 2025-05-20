package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerFetcher{

    private static String fetchApiData(String endpoint, int playerId) {
        String apiKey = System.getenv("MY_FOOTBALL_API_KEY");

        if(apiKey == null || apiKey.isEmpty()){
            System.err.println("Error, ENV not set");
            return null;
        }

        HttpURLConnection connection = null;

        try{
            String url = "https://v3.football.api-sports.io/" + endpoint + "?player=" + playerId;
            URL apiUrl = new URL(url);
            int attempt = 1;
            final int maxAttempts = 10;
            boolean isResponse200 = false;
            int httpCode;
            String responseData = null;

            while(attempt <= maxAttempts && !isResponse200){
                connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("x-apisports-key", apiKey);
                httpCode = connection.getResponseCode();

                if(httpCode == 200){
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                        String line;
                        StringBuilder response = new StringBuilder();
                        while((line = reader.readLine()) != null){
                            response.append(line);
                        }
                        responseData = response.toString();
                        isResponse200 = true;
                    }
                }else if(httpCode == 429){
                    System.err.println("Rate limited, retrying attempt: " + attempt + "/10");
                    attempt++;
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }else{
                    System.err.println("Error, HTTP code: " + httpCode);
                    break;
                }
                connection.disconnect();
            }

            return responseData;

        }catch(IOException e){
            System.err.println("Error: " + e.getMessage());
            return null;
        }finally{
            if(connection != null){
                connection.disconnect();
            }
        }
    }

    public static Player fetchPersonalData(int playerId){
        Player player = new Player(playerId);
        String responseData = fetchApiData("players/profiles", playerId);

        if (responseData == null) {
            return null;
        }

        player.appendData(responseData);
        player.parsePersonalDataJson();
        return player;
    }

    public static Player fetchClubData(int playerId){
        Player player = new Player(playerId);
        String responseData = fetchApiData("players/squads", playerId);

        if (responseData == null) {
            return null;
        }

        player.appendData(responseData);
        player.parseClubDataJson();
        return player;
    }

    public static Player fetchPlayer(int playerId){
        Player player = new Player(playerId);

        String personalData = fetchApiData("players/profiles", playerId);
        if (personalData == null) {
            return null;
        }
        player.appendData(personalData);
        player.parsePersonalDataJson();

        String clubData = fetchApiData("players/squads", playerId);
        if (clubData == null) {
            System.err.println("Warning: Unable to fetch club data for player " + playerId);
            return player; // Return with personal data only
        }
        player.appendData(clubData);
        player.parseClubDataJson();

        return player;
    }
}