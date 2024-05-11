package com.example.onlinequiz;

public class Question {
    private String question;
    private Integer questionNum;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;

    public Question(String q, Integer num, String ans1, String ans2, String ans3, String ans4){
        this.question = q;
        this.questionNum = num;
        this.answer1 = ans1;
        this.answer2 = ans2;
        this.answer3 = ans3;
        this.answer4 = ans4;
    }

    public String getQuestion(){
        return question;
    }
    public String getAnswer1(){
        return answer1;
    }
    public String getAnswer2(){
        return answer2;
    }
    public String getAnswer3(){
        return answer3;
    }
    public String getAnswer4(){
        return answer4;
    }


}
