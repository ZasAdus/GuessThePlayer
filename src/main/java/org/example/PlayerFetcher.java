package org.example;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerFetcher {

    public static Player fetchPlayer(int playerId) {
        Player player = new Player(playerId);
        String apiKey = System.getenv("MY_FOOTBALL_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error, ENV not set");
            return null;
        }

        HttpURLConnection connection = null;

        try {
            String url = "https://v3.football.api-sports.io/players/profiles?player=" + playerId;
            URL apiUrl = new URL(url);

            int attempt = 1;
            final int maxAttempts = 10;
            boolean isResponse200 = false;
            int httpCode = 0;

            while (attempt <= maxAttempts && !isResponse200) {
                connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("x-apisports-key", apiKey);

                httpCode = connection.getResponseCode();

                if (httpCode == 200) {
                    // Read the response
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        StringBuilder response = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        player.appendData(response.toString());
                        player.parseJson();
                        isResponse200 = true;
                    }
                } else if (httpCode == 429) {
                    System.err.println("Rate limited, retrying attempt: " + attempt + "/10");
                    attempt++;
                    try {
                        Thread.sleep(1000);  // Sleep for 1 second
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    System.err.println("Error, HTTP code: " + httpCode);
                    break;
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }

            if (!isResponse200) {
                return null;
            }

            return player;

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}