package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchDataFromApiFootball {
    public static String fetch(String url) {
        String apiKey = System.getenv("MY_FOOTBALL_API_KEY");
        if(apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error, ENV not set");
            return null;
       }
        HttpURLConnection connection = null;
        try {
            URL apiUrl = new URL(url);
            int attempt = 1;
            final int maxAttempts = 10;
            boolean isResponse200 = false;
            int httpCode;
            String responseData = null;

            while(attempt <= maxAttempts && !isResponse200) {
                connection =(HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("x-apisports-key", apiKey);
                httpCode = connection.getResponseCode();

                if(httpCode == 200) {
                    try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        StringBuilder response = new StringBuilder();
                        while((line = reader.readLine()) != null) {
                            response.append(line);
                       }
                        responseData = response.toString();
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
            return responseData;
       }catch(IOException e) {
            System.err.println("Error: " + e.getMessage());
       }finally {
            if(connection != null) {
                connection.disconnect();
           }
       }
        return null;
   }
}

