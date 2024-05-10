package com.example.onlinequiz;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class UserHomeController implements Initializable {
    @FXML
    private Button bt_start;

    @FXML
    private MenuItem mi_login;

    @FXML
    private MenuItem mi_logout;

    @FXML
    private Label lb_welcome;

    @FXML
    private Label lb_start;

    @FXML
    private ListView lv_summary;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add hover effect to registration button
        bt_start.setOnMouseClicked((MouseEvent event)  -> {
            bt_start.setStyle("-fx-background-color: #565051; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight:bold; -fx-width:85; -fx-height:39;");
        });
        //if server has sent the quiz, start label appears only....should be coded
    }
    public void loginMenuItemOnAction(ActionEvent event) throws IOException {
        // Load the login window
        try {
            // Load the login window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();
            UserLoginController loginController = loader.getController(); // Get the controller for login.fxml if needed

            // Create a new stage for the login window
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }
    public void logoutMenuItemOnAction(ActionEvent event) throws IOException {

        Stage homeStage = (Stage) bt_start.getScene().getWindow();
        homeStage.close();
    }
    public void startButtonOnAction(ActionEvent event) throws IOException{
        // Load the quiz window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quiz-view.fxml"));
            Parent root = loader.load();
            UserQuizController quizController = loader.getController();

            Stage quizStage = new Stage();
            quizStage.setTitle("Quiz");
            quizStage.setScene(new Scene(root));
            quizStage.show();
            bt_start.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void summaryChangeOnAction(ActionEvent event) {
    }
}
