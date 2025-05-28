package org.example.web;

import org.example.Player;

public class GameResult {
    private boolean correct;
    private String message;
    private Player correctPlayer;
    private Player guessedPlayer;
    private int attempts;

    public GameResult() {}

    public GameResult(boolean correct, String message, Player correctPlayer) {
        this.correct = correct;
        this.message = message;
        this.correctPlayer = correctPlayer;
    }

    public GameResult(boolean correct, String message, Player correctPlayer, Player guessedPlayer, int attempts) {
        this.correct = correct;
        this.message = message;
        this.correctPlayer = correctPlayer;
        this.guessedPlayer = guessedPlayer;
        this.attempts = attempts;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getMessage() {
        return message;
    }

    public Player getCorrectPlayer() {
        return correctPlayer;
    }

    public Player getGuessedPlayer() {
        return guessedPlayer;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCorrectPlayer(Player correctPlayer) {
        this.correctPlayer = correctPlayer;
    }

    public void setGuessedPlayer(Player guessedPlayer) {
        this.guessedPlayer = guessedPlayer;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}