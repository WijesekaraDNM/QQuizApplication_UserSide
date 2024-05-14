package com.example.onlinequiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class UserQuizController implements Initializable {
    @FXML
    private Button bt_home;

    @FXML
    private Button bt_finish;
    @FXML
    private Button bt_submit;

    @FXML
    private Label lb_question;
    @FXML
    private GridPane gp_showFinish;

    @FXML
    private Label lb_success;

    @FXML
    private RadioButton rb_a;
    @FXML
    private RadioButton rb_b;
    @FXML
    private RadioButton rb_c;
    @FXML
    private RadioButton rb_d;

    @FXML
    private Label lb_q_number;

    public static List<Question> quiz;

    private List<Label> questionLabels;

    private Integer currentQuestion=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionLabels = new ArrayList<>();
        // Add hover effect to buttons
//        bt_home.setOnMouseClicked((MouseEvent event) -> {
//            bt_home.setStyle("-fx-background-color: #565051; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight:bold; -fx-width:96; -fx-height:30;");
//        });
//        bt_submit.setOnMouseClicked((MouseEvent event) -> {
//            bt_submit.setStyle("-fx-background-color: #565051; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight:bold; -fx-width:66; -fx-height:30;");
//        });
        createQuestionPane();
        questionShow();
    }

    public void homeButtonOnAction(ActionEvent event) throws IOException {
        bt_home.getScene().getWindow().hide();

        // Load the login window
        try {
            // Load the login window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
            Parent root = loader.load();
            UserHomeController homeController = loader.getController();

            Stage homeStage = new Stage();
            homeStage.setTitle("Home");
            homeStage.setScene(new Scene(root));
            homeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //for testing
    public void getQuestions(){
        for(Question q: quiz){
            System.out.println("Question: " + q.getQuestion());
        }
    }

    public void questionShow() {
        if(currentQuestion != quiz.size()){
            Question q = quiz.get(currentQuestion);
            System.out.println(q.getQuestion());
            lb_question.setText(q.getQuestion());
            currentQuestion = currentQuestion + 1;
            lb_q_number.setText("Question: "+currentQuestion);
            rb_a.setText(q.getAnswer1());
            rb_b.setText(q.getAnswer2());
            rb_c.setText(q.getAnswer3());
            rb_d.setText(q.getAnswer4());
            if(currentQuestion == quiz.size()) {
                bt_submit.setVisible(false);
                bt_finish.setVisible(true);
            }
        }
    }

    public void finishButtonOnAction(ActionEvent event) throws IOException {
        submitAnswers();
        Stage quizStage = (Stage) bt_home.getScene().getWindow();
        quizStage.close();
        UserHomeController.showStage();
    }
    public void submitOnAction(ActionEvent event) {
        StringBuilder answers = new StringBuilder();
        String question = lb_q_number.getText() ;
        if(rb_a.isPressed()){
            answers.append("a,");
        }
        if (rb_b.isPressed()) {
            answers.append("b,");
        }
        if (rb_c.isPressed()) {
            answers.append("c,");
        }
        if (rb_d.isPressed()){
            answers.append("d");
        }
        if (answers.isEmpty()) {
            answers.append("No answer");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("answersFile.txt", true))) {
            writer.write(question + "," + answers.toString());
            writer.newLine();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void submitAnswers( ){
        if(rb_a.isSelected()||rb_b.isSelected()||rb_c.isSelected()||rb_d.isSelected()){
            String answers;
            if(rb_a.isSelected()){
                answers = "1";
            }
            else if (rb_b.isSelected()) {
                answers = "2";
            }
            else if (rb_c.isSelected()) {
                answers = "3";
            }
            else {
                answers = "4";
            }
            UserHomeController.client.sendAnswers("User:"+Data.username.substring(2,6)+","+currentQuestion+":"+answers);
            rb_a.setSelected(false);
            rb_b.setSelected(false);
            rb_c.setSelected(false);
            rb_d.setSelected(false);
            if(currentQuestion <= quiz.size()){
                colorChange(currentQuestion-1);
                questionShow();
            }

        }else{
            System.out.println("Select a answer");
        }
    }
    public void radioButtonOnAction(ActionEvent event) {
        if(event.getSource()==rb_a){
            rb_b.setSelected(false);
            rb_c.setSelected(false);
            rb_d.setSelected(false);
        }
        else if(event.getSource()==rb_b){
            rb_a.setSelected(false);
            rb_c.setSelected(false);
            rb_d.setSelected(false);
        }
        else if(event.getSource()==rb_c){
            rb_b.setSelected(false);
            rb_a.setSelected(false);
            rb_d.setSelected(false);
        }
        else if(event.getSource()==rb_d){
            rb_b.setSelected(false);
            rb_c.setSelected(false);
            rb_a.setSelected(false);
        }
    }
    public void createQuestionPane(){
        int sizeInt = quiz.size();
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        for(int i=0;i<sizeInt;i++) {
            Label newQ = customLabelQs(String.valueOf(i + 1));
            newQ.setEffect(dropShadow);

            gp_showFinish.setHalignment(newQ, HPos.CENTER);
            gp_showFinish.setPadding(new Insets(20));
            gp_showFinish.setHgap(3);
            gp_showFinish.setVgap(3);
            gp_showFinish.add(newQ, (i) % 3, (i) / 3);
            System.out.println("Question added to the pane!" + (i)/3 + " " + (i)%3);
            questionLabels.add(newQ);
        }
    }
    public void colorChange(int i) {
        Label label = questionLabels.get(i);
        label.setFont(new Font("Arial", 20));
        label.setStyle("-fx-border-color: white; -fx-background-color: #f77e05;");
        // Set text color
        label.setTextFill(Color.WHITE);
        // Set padding
        label.setPadding(new Insets(10));
    }
    public Label customLabelQs(String text){
        Label newLabel = new Label(text);
        newLabel.setFont(new Font("Arial", 20));
        newLabel.setStyle("-fx-border-color: black; -fx-background-color: lightgrey;");

        // Set text color
        newLabel.setTextFill(Color.WHITE);

        // Set padding
        newLabel.setPadding(new Insets(10));

        return newLabel;
    }

}