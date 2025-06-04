package org.example.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Player;

public class GuessRequest {
    @JsonProperty("randomPlayer")
    private Player randomPlayer;

    @JsonProperty("guess")
    private String guess;

    public GuessRequest() {
        // Default constructor for JSON deserialization
    }

    public GuessRequest(Player randomPlayer, String guess) {
        this.randomPlayer = randomPlayer;
        this.guess = guess;
    }

    public Player getRandomPlayer() {
        return randomPlayer;
    }

    public void setRandomPlayer(Player randomPlayer) {
        this.randomPlayer = randomPlayer;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    @Override
    public String toString() {
        return "GuessRequest{" +
                "randomPlayer=" + (randomPlayer != null ? randomPlayer.getFirstName() : "null") +
                ", guess='" + guess + '\'' +
                '}';
    }
}