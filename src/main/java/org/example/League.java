package org.example;

public class League {
    private String leagueName;
    private String logoURL;
    private Integer leagueID;
    League(Integer leagueID) {
        this.leagueID = leagueID;
    }
    League(String name, String logoURL, Integer leagueID) {
        this.leagueName = name;
        this.logoURL = logoURL;
        this.leagueID = leagueID;
    }
    public String getLeagueName() {
        return leagueName;
    }
    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }
    public String getLogoURL() {
        return logoURL;
    }
    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }
    public Integer getLeagueID() {
        return leagueID;
    }
    public void setLeagueID(Integer leagueID) {
        this.leagueID = leagueID;
    }
}
