package org.example.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuessResponse {
    @JsonProperty("correct")
    private boolean correct;

    @JsonProperty("guess")
    private String guess;

    @JsonProperty("message")
    private String message;

    public GuessResponse() {
        // Default constructor for JSON serialization
    }

    public GuessResponse(boolean correct, String guess) {
        this.correct = correct;
        this.guess = guess;
        this.message = correct ? "Correct guess!" : "Incorrect guess, try again!";
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GuessResponse{" +
                "correct=" + correct +
                ", guess='" + guess + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}