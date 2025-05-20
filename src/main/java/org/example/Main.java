package org.example;



public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <player_id>");
            System.exit(1);
        }

        try {
            int playerId = Integer.parseInt(args[0]);

            System.out.println("Fetching data for player ID: " + playerId);
            Player player = PlayerFetcher.fetchPlayer(playerId);

            if (player != null) {
                System.out.println("\nPlayer Details:");
                System.out.println("-----------------");
                player.display();
            } else {
                System.err.println("Failed to fetch player data.");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid player ID. Please enter a numeric ID.");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}