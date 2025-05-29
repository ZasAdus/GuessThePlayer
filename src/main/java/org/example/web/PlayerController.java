package org.example.web;

import org.example.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "http://localhost:3000")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Integer id) {
        Player player = playerService.getPlayerById(id);
        return player != null ? ResponseEntity.ok(player) : ResponseEntity.notFound().build();
    }


    @GetMapping("/random")
    public ResponseEntity<Player> getRandomPlayer() {
        return ResponseEntity.ok(playerService.getRandomPlayer());
    }

    @PostMapping("/guess")
    public ResponseEntity<GameResult> submitGuess(@RequestBody GuessRequest request) {
        GameResult result = playerService.checkGuess(request);
        return ResponseEntity.ok(result);
    }
}