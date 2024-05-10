package com.example.onlinequiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class UserQuizController implements Initializable {
    @FXML
    private Button bt_home;

    @FXML
    private Button bt_next;
    @FXML
    private Button bt_submit;

    @FXML
    private Label lb_quiz;

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




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Add hover effect to buttons
        bt_home.setOnMouseClicked((MouseEvent event) -> {
            bt_home.setStyle("-fx-background-color: #565051; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight:bold; -fx-width:96; -fx-height:30;");
        });
        bt_next.setOnMouseClicked((MouseEvent event) -> {
            bt_next.setStyle("-fx-background-color: #565051; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight:bold; -fx-width:63; -fx-height:30;");
        });
        bt_submit.setOnMouseClicked((MouseEvent event) -> {
            bt_submit.setStyle("-fx-background-color: #565051; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight:bold; -fx-width:66; -fx-height:30;");
        });

        }

    public void homeButtonOnAction(ActionEvent event) throws IOException {
        Stage quizStage = (Stage) bt_home.getScene().getWindow();
        quizStage.close();

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

    public void nextButtonOnAction(ActionEvent event) {
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
        lb_success.setText("Successfully Submitted!");
    }
    public void quizChangeOnAction(ActionEvent event) {
    }
    public void answerGettingOnAction(ActionEvent event) {

    }
}