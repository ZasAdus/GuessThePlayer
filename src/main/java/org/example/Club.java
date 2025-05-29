package org.example;

public class Club {
    private Integer clubID;
    private String name;
    private String logoURL;
    private League league;

    Club(Integer clubID){
        this.clubID = clubID;
    }
    Club(Integer clubID, String name, String logoURL, League league) {
        this.clubID = clubID;
        this.name = name;
        this.logoURL = logoURL;
        this.league = league;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public Integer getClubID() {
        return clubID;
    }

    public void setClubID(Integer clubID) {
        this.clubID = clubID;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}
