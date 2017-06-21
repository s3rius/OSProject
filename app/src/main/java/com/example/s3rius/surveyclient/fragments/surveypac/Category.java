package com.example.s3rius.surveyclient.fragments.surveypac;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {

    private String       name;
    private List<Survey> surveys;

    public String getName(){
        return name;
    }

    public Category(){
    }

    public Category setName( String name ){
        this.name = name;
        return this;
    }

    public List<Survey> getSurveys(){
        return surveys;
    }

    public void setSurveys( List<Survey> surveys ){
        this.surveys = surveys;
    }
}
