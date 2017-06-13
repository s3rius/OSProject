package com.example.s3rius.surveyclient.fragments.surveypac;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Survey implements Serializable {

    private Integer id;
    private String name;
    private String comment;
    private List<User> users;
    private List<Question> questions;
    private User madeByUser;
    private Date date;
    private String category;

    public Survey() {
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setMadeByUser(User madeByUser) {
        this.madeByUser = madeByUser;
    }

    @JsonIgnore
    public boolean isAllAnswered() {
        for (int i = 0; i < questions.size(); i++) {
            if (!questions.get(i).isAllAnswered()) {
                return false;
            }
        }
        return true;
    }
}
