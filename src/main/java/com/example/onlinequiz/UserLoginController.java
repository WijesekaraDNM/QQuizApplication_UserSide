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
    import java.io.BufferedReader;
    import java.io.FileReader;

public class UserLoginController implements Initializable {
    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField pf_password;

    @FXML
    private Button bt_login;

    @FXML
    private Button bt_cancel;

    @FXML
    private Button bt_registrationNavigation;

    @FXML
    private Label lb_login;

    @FXML
    private ImageView brandingImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Add hover effect to login button
        bt_login.setOnMouseClicked((MouseEvent event) -> {
            bt_login.setStyle("-fx-background-color: #f77e05; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight:bold; -fx-width:61; -fx-height:30;");
        });
    }

//    private String username = "Masha";
//    private String password = "quiz";

    public void loginButtonOnAction(ActionEvent event) throws IOException {
        String username = tf_username.getText();
        String password = pf_password.getText();
        if(!username.isBlank() && !password.isBlank()){
            if(loginValidation(username,password)){

                Data.username = username;
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
                    Parent root = loader.load();
                    UserHomeController homeController = loader.getController(); // Get the controller for login.fxml if needed

                    // Create a new stage for the login window
                    Stage homeStage = new Stage();
                    homeStage.setTitle("Home");
                    homeStage.setScene(new Scene(root));
                    homeStage.show();

                }catch (IOException e) {
                    e.printStackTrace(); // Handle or log the exception appropriately
                }

                bt_login.getScene().getWindow().hide();
            }else{
                lb_login.setText("Invalid username or password!");
            }
        }else{
            lb_login.setText("Enter username and password!");
        }
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) bt_cancel.getScene().getWindow();
        stage.close();
    }

    public void registrationNavigationOnAction(ActionEvent event) {
        Stage loginStage = (Stage) bt_registrationNavigation.getScene().getWindow();
        loginStage.close();
        // Load the login window
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registration-view.fxml"));
            Parent root = loader.load();
            UserRegistrationController registrationController = loader.getController(); // Get the controller for login.fxml if needed

            // Create a new stage for the login window
            Stage registrationStage = new Stage();
            registrationStage.setTitle("Registration");
            registrationStage.setScene(new Scene(root));
            registrationStage.show();

        }catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    public boolean loginValidation(String username, String password){
        try (BufferedReader reader = new BufferedReader(new FileReader("credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true; // Username and password match found
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
        return false;
    }
}