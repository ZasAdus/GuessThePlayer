package org.example;

public class Player{
    private final Integer playerId;
    private String firstName;
    private String lastName;
    private String position;
    private Integer age;
    private Club club;
    private Nationality nationality;
    private String photoURL;
    private Integer shirtNumber;

    public Player(int id){
        this.playerId = id;
    }

    public Integer getShirtNumber(){
        return shirtNumber;
    }
    public Integer getPlayerId(){
        return playerId;
    }
    public Integer getAge(){
        return age;
    }
    public Nationality getNationality(){
        return nationality;
    }
    public String getFirstName(){return firstName;}
    public String getLastName(){
        return lastName;
    }
    public String getPosition(){
        return position;
    }
    public Club getClub(){
        return club;
    }
    public String getPhotoURL(){
        return photoURL;
    }

    public void setShirtNumber(Integer shirtNumber) {
        this.shirtNumber = shirtNumber;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setAge(Integer age){
        this.age = age;
    }
    public void setLastName(String lastName){this.lastName = lastName;}
    public void setPosition(String position){
        this.position = position;
    }
    public void setClub(Club club){
        this.club = club;
    }
    public void setNationality(Nationality nationality){
        this.nationality = nationality;
    }
    public void setPhotoURL(String photoURL){
        this.photoURL = photoURL;
    }

}