package com.example.s3rius.surveyclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SurveyClass {
    private ArrayList<Question> questions = new ArrayList<>();

//    private void normalize(){
//        int max = 0;
//        for (int i = 0; i < numberOfAnswers.length; i++) {
//            if (numberOfAnswers[i] > max){
//                max=numberOfAnswers[i];
//            }
//        }
//        answers = new String[numberOfAnswers.length][max];
//    }

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

        public String[] getAnswers() {
            return answers;
        }
    }
}
