package com.example.s3rius.surveyclient.fragments.surveypac;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Survey implements Serializable {

    private Integer        id;
    private String         name;
    private String         comment;
    private List<User>     users;
    private List<Question> questions;
    private User           madeByUser;
    private Date           date;

    public Date getDate(){
        return date;
    }

    public void setDate( Date date ){
        this.date = date;
    }


    public Survey(){
    }


    public Integer getId(){
        return id;
    }

    public List<Question> getQuestions(){
        return questions;
    }

    public void setQuestions( List<Question> questions ){
        this.questions = questions;
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

    public String getComment(){
        return comment;
    }

    public void setComment( String comment ){
        this.comment = comment;
    }

    public void setUsers( List<User> users ){
        this.users = users;
    }

    public void setMadeByUser( User madeByUser ){
        this.madeByUser = madeByUser;
    }
}
