package org.example.web;

import org.example.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH})
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Integer id) {
        Player player = playerService.getPlayerById(id);
        return player != null ? ResponseEntity.ok(player) : ResponseEntity.notFound().build();
    }

    @GetMapping("/random")
    public ResponseEntity<Player> getRandomPlayer() {
        return ResponseEntity.ok(playerService.getRandomPlayer());
    }

    @GetMapping("/fullName/{fullName}")
    public ResponseEntity<Player> getPlayerByFullName(@PathVariable String fullName) {
        Player player = playerService.getPlayerByFullName(fullName);
        return player != null ? ResponseEntity.ok(player) : ResponseEntity.notFound().build();
    }

    @PostMapping("/guess")
    public ResponseEntity<GuessResponse> checkGuess(@RequestBody GuessRequest guessRequest) {
        try {
            System.out.println("Received guess request: " + guessRequest);
            System.out.println("Raw request body - Guess: " + guessRequest.getGuess());
            System.out.println("Raw request body - Random Player: " + (guessRequest.getRandomPlayer() != null ? guessRequest.getRandomPlayer().getFirstName() : "null"));

            if (guessRequest.getGuess() == null || guessRequest.getGuess().trim().isEmpty()) {
                GuessResponse errorResponse = new GuessResponse(false, "");
                errorResponse.setMessage("Guess cannot be empty");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            if (guessRequest.getRandomPlayer() == null) {
                GuessResponse errorResponse = new GuessResponse(false, guessRequest.getGuess());
                errorResponse.setMessage("Random player data is missing");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            boolean isCorrect = playerService.checkGuess(guessRequest.getRandomPlayer(), guessRequest.getGuess());
            GuessResponse response = new GuessResponse(isCorrect, guessRequest.getGuess());
            System.out.println("Sending response: " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error processing guess: " + e.getMessage());
            e.printStackTrace();
            GuessResponse errorResponse = new GuessResponse(false, guessRequest != null ? guessRequest.getGuess() : "unknown");
            errorResponse.setMessage("Error processing guess: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}