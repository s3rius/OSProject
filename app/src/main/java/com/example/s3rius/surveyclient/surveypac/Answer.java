package com.example.s3rius.surveyclient.surveypac;

import java.io.Serializable;

public class Answer implements Serializable {
    private String name;
    private Integer answered;

    public Answer() {
    }

    public Answer(String name, Integer answered) {
        this.name = name;
        this.answered = answered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAnswered() {
        return answered;
    }

    public void setAnswered(Integer answered) {
        this.answered = answered;
    }
}
