package com.example.s3rius.surveyclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SurveyClass {
    private ArrayList<Question> questions = new ArrayList<>();

    public void addquestion(String question, String[] answers){
        questions.add(new Question(question, answers));
    }

    public int getQuantityOfQuestions() {
        return questions.size();
    }

    public Question getQuestionAt(int position) {
        return questions.get(position);
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return super.toString();
    }
    public class Question{
        String question;
        String[] answers;
        public Question(String question, String[] answers){
            this.question = question;
            this.answers = answers;
        }
        public String getQuestion() {
            return question;
        }

        public long getQuantityOfAnswers(){
            return answers.length;
        }

        public String getAnswerAt(int position){
            return answers[position];
        }

        public String[] getAnswers() {
            return answers;
        }
    }
}
