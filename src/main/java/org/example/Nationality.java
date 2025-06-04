package org.example;

public class Nationality {
    private String countryName;
    private String logoURL;

    Nationality(String name) {
        this.countryName = name;
    }
    Nationality(){}

    Nationality(String name, String logoURL) {
        this.countryName = name;
        this.logoURL = logoURL;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String name) {
        this.countryName = name;
    }

    public String getLogoURL() {
        return this.logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }
}