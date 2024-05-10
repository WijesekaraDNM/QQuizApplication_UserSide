package com.example.onlinequiz;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

public class UserRegistrationController implements Initializable {
    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField pf_password;

    @FXML
    private PasswordField pf_confirmPassword;

    @FXML
    private Button bt_cancel;

    @FXML
    private Button bt_loginNavigation;

    @FXML
    private Button bt_registration;

    @FXML
    private Label lb_registration;

    @FXML
    private ImageView brandingImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        File brandingFile = new File("/images/logo.png");
//        Image brandingImage = new Image(brandingFile.toURI().toString());
//        brandingImageView.setImage(brandingImage);

        // Add hover effect to registration button
        bt_registration.setOnMouseClicked((MouseEvent event) -> {
            bt_registration.setStyle("-fx-background-color: #f77e05; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight:bold; -fx-width:61; -fx-height:30;");
        });

    }



    private String username;
    private String password;

    public void registrationButtonOnAction(ActionEvent event) throws IOException {
        String username = tf_username.getText();
        String password = pf_password.getText();
        String confirmPassword = pf_confirmPassword.getText();
        if (!username.isBlank() && !password.isBlank() && !confirmPassword.isBlank()) {
            if (registrationValidation()) {

                // Save username and password to a file
                saveCredentialsToFile(username, password);
                Stage registrationStage = (Stage) bt_loginNavigation.getScene().getWindow();
                registrationStage.close();

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
        } else {
            lb_registration.setText("Enter username and password!");
        }
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) bt_cancel.getScene().getWindow();
        stage.close();
    }
    public void loginNavigationOnAction(ActionEvent event) {
        Stage registrationStage = (Stage) bt_loginNavigation.getScene().getWindow();
        registrationStage.close();

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
    public boolean registrationValidation() {
        if (Objects.equals(pf_password.getText(),pf_confirmPassword.getText())) {
            lb_registration.setText("Register is successful!");
            return true;
        } else {
            lb_registration.setText("Passwords are not matching!");
            return false;
        }
    }
    private void saveCredentialsToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("credentials.txt", true))) {
            writer.write(username + "," + password); // Write username and password to file
            writer.newLine(); // Add a new line for the next entry (if any)
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
