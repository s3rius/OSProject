package com.example.s3rius.surveyclient;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class User {

    private Integer      id;
    private String       name;
    private String       lastName;
    private String       login;
    private String       password;
    private List<Survey> surveys;
    private UserRoles    role;


    public User(){
    }

    public Integer getId(){
        return id;
    }

    public void setId( Integer id ){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName( String lastName ){
        this.lastName = lastName;
    }

    public String getLogin(){
        return login;
    }

    public void setLogin( String login ){
        this.login = login;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword( String password ){
        this.password = password;
    }

    public UserRoles getRole(){
        return role;
    }

    public void setRole( UserRoles role ){
        this.role = role;
    }

    public List<Survey> getSurveys(){
        return surveys;
    }

    public void setSurveys( List<Survey> surveys ){
        this.surveys = surveys;
    }



    public String getUsername(){
        return this.getLogin();
    }


    public boolean isAccountNonExpired(){
        return false;
    }


    public boolean isAccountNonLocked(){
        return false;
    }

    public boolean isCredentialsNonExpired(){
        return false;
    }

    public boolean isEnabled(){
        return true;
    }
}
