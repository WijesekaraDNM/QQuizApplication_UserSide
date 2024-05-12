package com.example.onlinequiz;

    import javafx.scene.control.Label;

    import java.io.*;
    import java.net.Socket;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Scanner;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

public class Client extends Thread{
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Socket server;
    private int clientId;

    private Label markLabel;

    Client(Socket server, Label marks) throws IOException {

        try{
            this.server = server;
            this.markLabel = marks;
            this.bufferedReader = new BufferedReader(new InputStreamReader(server.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            while(server.isConnected()) {
                String messageFromServer = bufferedReader.readLine();
                System.out.println("Message: "+messageFromServer );
                messageProcess(messageFromServer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error receiving question from server");
            closeEverything(server,bufferedReader, bufferedWriter);
        } finally {
            String endMessage = "Server disconnected";
            System.out.println(endMessage);
            closeEverything(server, bufferedReader, bufferedWriter);
        }
    }

    public void messageProcess(String message){
        String patternUserName = "Client(\\d+):username";
        String patternQuiz = "QuizName:(\\w+)";
        String patternMarks = "Quiz:(\\d+)";

        Pattern regexN = Pattern.compile(patternQuiz);
        Matcher matcherN = regexN.matcher(message);

        if(message.matches(patternUserName)){
            Pattern regex = Pattern.compile(patternUserName);
            Matcher matcher = regex.matcher(message);
            if(matcher.find()) {
                clientId = Integer.parseInt(matcher.group(1));
                System.out.println(matcher.group(1));
                sendUsername(Data.username);
            }

        } else if (matcherN.find()) {
            String qName;
            List<Question> questions = new ArrayList<>();
            String patternQ = "question:(.*?),1:(.*?),2:(.*?),3:(.*?),4:(.*?)";

            String[] parts = message.split(";");

            Pattern regexName = Pattern.compile(patternQuiz);
            Matcher matcherName = regexName.matcher(parts[0]);

            if(matcherName.find()){
                qName = matcherName.group(1);
                UserHomeController.quizName = qName;
                System.out.println("Quiz name: " + qName);
            }

            for(int i=1; i< parts.length; i++){
                System.out.println("Message Part"+i+":"+parts[i]);
                Pattern regexQ = Pattern.compile(patternQ);
                Matcher matcherQ = regexQ.matcher(parts[i]);
                if(matcherQ.matches()){
                    String question = matcherQ.group(1);
                    String answer1 = matcherQ.group(2);
                    String answer2 = matcherQ.group(3);
                    String answer3 = matcherQ.group(4);
                    String answer4 = matcherQ.group(5);
                    Question q = new Question(question,i,answer1,answer2,answer3,answer4);
                    questions.add(q);
                    System.out.println("Question added");
                }
            }

            if(questions.isEmpty()){
                System.out.println("Question list empty");
            }else{
                UserQuizController.quiz = questions;
                System.out.println("Question list obtained: "+questions.size());
            }

        } else if(message.matches(patternMarks)){
            Pattern regex = Pattern.compile(patternMarks);
            Matcher matcher = regex.matcher(message);
            //Matcher matcher = Pattern.compile(patternUserName).matcher(message);

            if(matcher.find()){
                String marks = matcher.group(1);
                System.out.println("Marks obtained: "+ marks);
                UserHomeController.marksDisplay(marks, markLabel);
            }
        }
    }

    public void sendAnswers(String answer){
        try {
            bufferedWriter.write(answer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Answer send: "+answer);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending answers to server");
        }
    }

    public void sendUsername(String username){
        String pattern = "EG(\\d+)";
        if( username.matches(pattern)){
            Pattern regex = Pattern.compile(pattern);
            Matcher matcher = regex.matcher(username);
            if(matcher.find()) {
                Integer userNumber = Integer.valueOf(matcher.group(1));
                System.out.println("Sending username..." + userNumber);
                try {
                    bufferedWriter.write("User"+userNumber+":Registered");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error sending username to server");
                }

            }
        }
    }

    public void closeEverything(Socket server, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(server != null){
                server.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
