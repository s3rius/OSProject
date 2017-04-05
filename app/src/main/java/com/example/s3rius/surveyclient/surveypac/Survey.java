package com.example.s3rius.surveyclient.surveypac;

import java.util.List;

public class Survey {

    private String name;
    private String comment;
    private List<Question> questions;
    private List<User> users;

    public Survey() {
    }

    public Survey(String name, String comment, List<Question> questions, List<User> users) {
        this.name = name;
        this.comment = comment;
        this.questions = questions;
        this.users = users;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
