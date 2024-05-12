package com.example.onlinequiz;

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

    Client(Socket server) throws IOException {

        this.server = server;

        this.bufferedReader = new BufferedReader(new InputStreamReader(server.getInputStream()));
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
    }

    @Override
    public void run(){
        try {
            while(server.isConnected()) {
                String messageFromServer = bufferedReader.readLine();
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
        String patternQuiz = "QuizName:(\\w+);";
        String patternMarks = "Quiz:(\\d+)";

        System.out.println(message);

        if(message.matches(patternUserName)){
            Pattern regex = Pattern.compile(patternUserName);
            Matcher matcher = regex.matcher(message);
            if(matcher.find()) {
                clientId = Integer.getInteger(matcher.group(1));
                sendUsername(Data.username);
            }

        } else if (message.matches(patternQuiz)) {
            String qName;
            List<Question> questions = new ArrayList<>();
            String patternQ = "question:(\\w+),1:(\\w+),2:(\\w+),3:(\\w+),4:(\\w+)";

            String[] parts = message.split(";");

            Pattern regexName = Pattern.compile(patternQuiz);
            Matcher matcherName = regexName.matcher(parts[0]);

            Pattern regexQ = Pattern.compile(patternQ);

            for(int i=0; i< parts.length; i++){
                if(matcherName.find()){
                    qName = matcherName.group(1);
                    UserHomeController.quizName = qName;
                }else{
                    Matcher matcherQ = regexQ.matcher(parts[i]);
                    if(matcherQ.find()){
                        String question = matcherQ.group(1);
                        String answer1 = matcherQ.group(2);
                        String answer2 = matcherQ.group(3);
                        String answer3 = matcherQ.group(4);
                        String answer4 = matcherQ.group(5);
                        Question q = new Question(question,i,answer1,answer2,answer3,answer4);
                        questions.add(q);
                    }
                }
            }
            UserQuizController.quiz = questions;
            System.out.println("Question list obtained");

        } else if(message.matches(patternMarks)){
            Pattern regex = Pattern.compile(patternMarks);
            Matcher matcher = regex.matcher(message);
            //Matcher matcher = Pattern.compile(patternUserName).matcher(message);

            if(matcher.find()){
                String marks = matcher.group(1);
                UserHomeController.marksDisplay(marks);
            }
        }
    }

    public void sendAnswers(String answer){
        try {
            bufferedWriter.write(answer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
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
                Integer userNumber = Integer.getInteger(matcher.group(1));
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
