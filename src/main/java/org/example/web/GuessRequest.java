package org.example.web;

public class GuessRequest {
    private Integer playerId;
    private String guessedName;
    private String timestamp;

    public GuessRequest() {}

    public GuessRequest(Integer playerId, String guessedName, String timestamp) {
        this.playerId = playerId;
        this.guessedName = guessedName;
        this.timestamp = timestamp;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public String getGuessedName() {
        return guessedName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public void setGuessedName(String guessedName) {
        this.guessedName = guessedName;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}