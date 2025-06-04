package org.example;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Player{
    @JsonProperty("id")
    private Integer playerId;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("position")
    private String position;
    @JsonProperty("age")
    private Integer age;
    @JsonProperty("club")
    private Club club;
    @JsonProperty("nationality")
    private Nationality nationality;
    @JsonProperty("photoURL")
    private String photoURL;
    @JsonProperty("shirtNumber")
    private Integer shirtNumber;

    public Player() {
    }
    public Player(int id){
        this.playerId = id;
    }

    public Integer getShirtNumber(){
        return this.shirtNumber;
    }
    public Integer getPlayerId(){
        return this.playerId;
    }
    public Integer getAge(){
        return this.age;
    }
    public Nationality getNationality(){
        return this.nationality;
    }
    public String getFirstName(){return this.firstName;}
    public String getLastName(){
        return this.lastName;
    }
    public String getPosition(){
        return this.position;
    }
    public Club getClub(){
        return this.club;
    }
    public String getPhotoURL(){
        return this.photoURL;
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