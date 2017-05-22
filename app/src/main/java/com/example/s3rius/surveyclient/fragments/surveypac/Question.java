package com.example.s3rius.surveyclient.fragments.surveypac;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String name;
    private List<Answer> answers;

    public Question() {
    }

    public Question(String name, List<Answer> answers) {
        this.name = name;
        this.answers = answers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
