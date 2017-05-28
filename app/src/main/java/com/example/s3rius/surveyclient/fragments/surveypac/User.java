package com.example.s3rius.surveyclient.fragments.surveypac;


import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String name;
    private String lastName;
    private String login;
    private String password;
    private List<Survey> madeSurveys;
    private UserRoles role;
    private List<Survey> doneSurveys;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Survey> getMadeSurveys() {
        return madeSurveys;
    }

    public void setMadeSurveys(List<Survey> madeSurveys) {
        this.madeSurveys = madeSurveys;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }


    public List<Survey> getDoneSurveys() {
        return doneSurveys;
    }

    public void setDoneSurveys(List<Survey> doneSurveys) {
        this.doneSurveys = doneSurveys;
    }
}
