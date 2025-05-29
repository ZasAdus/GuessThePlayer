package org.example.web;

import org.example.Database;
import org.example.Player;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;

@Service
class PlayerService {

    PlayerService(ResourcePatternResolver resourcePatternResolver) {
    }

    public List<Player> getAllPlayers() {
        Database.connect();
        List<Player> players = Database.getPlayersFromDatabase();
        Database.disconnect();
        return players;
    }

    public Player getPlayerById(Integer id) {
        Database.connect();
        Player player = Database.getPlayerFromDatabase(id);
        Database.disconnect();
        return player;
    }

    public Player getRandomPlayer(){
        List<Player> players = getAllPlayers();
        SecureRandom secureRandom = new SecureRandom();
        int random = secureRandom.nextInt(players.size());
        return players.get(random);
    }

    public GameResult checkGuess(GuessRequest request) {
        return new GameResult();
    }
}
