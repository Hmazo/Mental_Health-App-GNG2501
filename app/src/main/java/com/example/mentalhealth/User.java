package com.example.mentalhealth;

public class User {

    private String name;
    private String email;
    private String date;
    private String password;
    private String profilPicture;
    private String userID;
    private int abonne;
    private int abonnements;
    private String bio;
    private int pubNumber;

    public User() {

    }

    public User(String name, String email, String date, String password, String userID) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.password = password;
        this.profilPicture = "default";
        this.userID = userID;
        abonne = 0;
        abonnements = 0;
        pubNumber =0;
        bio= "";
    }

    public String getName() {
        return name;
    }

    public int getAbonne() {
        return abonne;
    }

    public void setAbonne(int abonne) {
        this.abonne = abonne;
    }

    public int getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(int abonnements) {
        this.abonnements = abonnements;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getPubNumber() {
        return pubNumber;
    }

    public void setPubNumber(int pubNumber) {
        this.pubNumber = pubNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilPicture() {
        return profilPicture;
    }

    public void setProfilPicture(String profilPicture) {
        this.profilPicture = profilPicture;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
