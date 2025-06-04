package org.example.web;

import org.example.Database;
import org.example.Player;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
class PlayerService {

    PlayerService(ResourcePatternResolver resourcePatternResolver) {
    }

    public Player getPlayerById(Integer id) {
        return Database.getPlayerFromDatabase(id);
    }

    public Player getPlayerByFullName(String fullName) {
        return Database.getPlayerFromDatabaseFullName(fullName);
    }

    public Player getRandomPlayer(){
        SecureRandom random = new SecureRandom();
        Player player = Database.getPlayerFromDatabase(random.nextInt(1,Database.getNumberOfPlayers()+1));
        return player;
    }

    public boolean checkGuess(Player randomPlayer, String guess) {
        //TODO
        return true;
    }
}
