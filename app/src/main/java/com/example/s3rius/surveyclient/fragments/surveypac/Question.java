package com.example.s3rius.surveyclient.fragments.surveypac;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private Integer id;
    private String name;
    private List<Answer> answers;


    public Question() {
    }

    public Question(String name, ArrayList<Answer> answers) {
        this.name = name;
        this.answers = answers;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void uncheck() {
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setAnswered(false);
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @JsonIgnore
    public boolean isAllAnswered() {
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).isAnswered()) {
                return true;
            }
        }
        return false;
    }
}
