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
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


import java.io.File;
import java.io.IOException;
import java.net.Socket;
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
    private Label lb_availability;

    @FXML
    private static Label lb_marks;

    @FXML
    private Label lb_start;
    @FXML
    private Label lb_user;

    @FXML
    private ListView lv_summary;
    public static String quizName;
    public static String marks;
    public static Client client;

    public static Thread quizThread;
    private static Stage primaryStage;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add hover effect to registration button
        bt_start.setOnMouseClicked((MouseEvent event)  -> {
            bt_start.setStyle("-fx-background-color: #565051; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight:bold; -fx-width:85; -fx-height:39;");
        });
        displayUserName();
        quizFinder();
        //if server has sent the quiz, start label appears only....should be coded
    }
    public static void setStage(Stage home){
        primaryStage = home;
    }
    public static void showStage(){
        primaryStage.show();
    }
    public void quizFinder(){
       quizThread=new Thread(new Runnable() {
           @Override
           public void run() {
               boolean isNotConnected = true;
               while(isNotConnected){
                   try {
                       client = new Client(new Socket("Localhost",1234));
                       client.start();
                       System.out.println("Connected to a server");
                       displayStartLabel();
                       isNotConnected = false;
                   }catch (IOException e) {
                       System.out.println("Server not connected");
                   }
               }
           }
       });
       quizThread.start();
    }

    public void displayStartLabel(){
        lb_availability.setText(quizName);
        lb_start.setVisible(true);
    }
    public void displayUserName(){
        String user = Data.username;
        lb_user.setText(user);
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
    public static void marksDisplay(String Marks) {
        marks = Marks;
        lb_marks.setText(marks);
        lb_marks.setVisible(true);
        ObservableList<String> marksList = FXCollections.observableArrayList(marks.split(","));

        // Set the ObservableList as the items of the ListView
        lv_summary.getItems().add(marks);
    }



}
