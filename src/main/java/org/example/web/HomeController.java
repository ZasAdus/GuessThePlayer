package org.example.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "<h1>Guess the Player API</h1>" +
                "<p>API is running successfully!</p>" +
                "<h3>Available endpoints:</h3>" +
                "<ul>" +
                "<li>GET /api/players - Get all players</li>" +
                "<li>GET /api/players/random - Get random player</li>" +
                "<li>GET /api/players/{id} - Get player by ID</li>" +
                "<li>POST /api/players/guess - Submit a guess</li>" +
                "</ul>";
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}