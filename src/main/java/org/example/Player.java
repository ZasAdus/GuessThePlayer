package org.example;

public class Player{
    private final Integer playerId;
    private String firstName;
    private String lastName;
    private String position;
    private Integer clubID;
    private Integer nationalityID;
    private String nationality;
    private String photoURL;

    public Player(int id){
        this.playerId = id;
    }

    public Integer getId(){
        return playerId;
    }

    public String getNationality(){
        return nationality;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getPosition(){
        return position;
    }

    public Integer getClubID(){
        return clubID;
    }

    public Integer getNationalityID(){
        return nationalityID;
    }

    public String getPhotoURL(){
        return photoURL;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setPosition(String position){
        this.position = position;
    }
    public void setClubID(Integer clubID){
        this.clubID = clubID;
    }
    public void setNationality(String nationality){
        this.nationality = nationality;
    }
    public void setPhotoURL(String photoURL){
        this.photoURL = photoURL;
    }
    public void setNationalityID(Integer nationalityID){
        this.nationalityID = nationalityID;
    }

    public void display(){
        System.out.println("Name: " + firstName + " " + lastName);
        if(position != null && !position.isEmpty()){
            System.out.println("Position: " + position);
        }
        if(clubID != null){
            System.out.println("ClubID: " + clubID);
        }
        if(nationality != null && !nationality.isEmpty()){
            System.out.println("Country: " + nationality);
        }
        System.out.println("Player ID: " + playerId);
    }

}