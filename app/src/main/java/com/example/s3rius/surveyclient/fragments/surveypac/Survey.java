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
    private List<UserAnswer> answers;
    private User creator;
    private Date date;
    private Category category;

    public Survey() {
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

    public List<UserAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<UserAnswer> answers) {
        this.answers = answers;
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

    @JsonIgnore
    public boolean isAllAnswered() {
        for (int i = 0; i < questions.size(); i++) {
            if (!questions.get(i).isAllAnswered()) {
                return false;
            }
        }
        return true;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
