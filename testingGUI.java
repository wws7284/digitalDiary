import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;

public class testingGUI extends Application {

    private static final String USER_FILE = "users.csv";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("E-Diary");

        // Create the main layout
        BorderPane root = new BorderPane();

        // Create the scene for Welcome page
        Scene welcomeScene = createWelcomePage(primaryStage);
        root.setCenter(welcomeScene.getRoot());
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }

    private Scene createWelcomePage(Stage primaryStage) {
        VBox welcomeBox = new VBox(20);
        welcomeBox.setPadding(new Insets(20));
        welcomeBox.setAlignment(Pos.CENTER); // Center align

        Label welcomeLabel = new Label("Welcome to E-Diary");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button registerButton = new Button("Register");
        Button loginButton = new Button("Login");
        Button exitButton = new Button("Exit");

        registerButton.setOnAction(e -> primaryStage.setScene(createRegisterPage(primaryStage)));
        loginButton.setOnAction(e -> primaryStage.setScene(createLoginPage(primaryStage)));
        exitButton.setOnAction(e -> System.exit(0));

        welcomeBox.getChildren().addAll(welcomeLabel, registerButton, loginButton, exitButton);

        return new Scene(welcomeBox, 500, 500);
    }

    private Scene createRegisterPage(Stage primaryStage) {
        VBox registerBox = new VBox(10);
        registerBox.setPadding(new Insets(20));

        Label registerLabel = new Label("Register New User");
        registerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username: ");
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Label emailLabel = new Label("Email: ");
        emailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");

        Label passwordLabel = new Label("Password: ");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: red;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button registerSubmitButton = new Button("Register");
        registerSubmitButton.setOnAction(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in all fields");
                return; // Don't proceed with registration
            }

            // Proceed with registration if fields are not empty
            registerUser(username, email, password);
        });

        // Set actions for "Enter" key
        usernameField.setOnAction(e -> emailField.requestFocus());
        emailField.setOnAction(e -> passwordField.requestFocus());
        passwordField.setOnAction(e -> registerSubmitButton.fire());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(createWelcomePage(primaryStage)));

        registerBox.getChildren().addAll(registerLabel, usernameLabel, usernameField, emailLabel, emailField,
                passwordLabel, passwordField,
                registerSubmitButton,
                backButton);

        return new Scene(registerBox, 500, 500);
    }

    private Scene createLoginPage(Stage primaryStage) {
        VBox loginBox = new VBox(20);
        loginBox.setPadding(new Insets(20));

        Label loginLabel = new Label("User Login");
        loginLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField loginUserField = new TextField();
        loginUserField.setPromptText("Enter username or email");

        PasswordField loginPasswordField = new PasswordField();
        loginPasswordField.setPromptText("Enter password");

        Button loginSubmitButton = new Button("Login");
        loginSubmitButton.setOnAction(e -> loginUser(loginUserField.getText(), loginPasswordField.getText()));

        // Set action for "Enter" key
        loginUserField.setOnAction(e -> loginPasswordField.requestFocus());
        loginPasswordField.setOnAction(e -> loginSubmitButton.fire());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(createWelcomePage(primaryStage)));

        loginBox.getChildren().addAll(loginLabel, loginUserField, loginPasswordField, loginSubmitButton, backButton);

        return new Scene(loginBox, 500, 500);
    }

    private void registerUser(String userName, String email, String password) {
        if (isUserExists(userName, email)) {
            showAlert("Error", "Username or email already exists!");
            return;
        }

        try (FileWriter writer = new FileWriter(USER_FILE, true)) {
            writer.write(userName + "," + email + "," + password + "\n");
            showAlert("Success", "Registration successful! Welcome, " + userName);
        } catch (IOException e) {
            showAlert("Error", "Unable to save user data.");
            e.printStackTrace();
        }
    }

    private void loginUser(String userInput, String password) {
        if (authenticateUser(userInput, password)) {
            showAlert("Success", "Login successful! Welcome back.");
        } else {
            showAlert("Error", "Invalid username/email or password.");
        }
    }

    private boolean isUserExists(String userName, String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if (userDetails[0].equals(userName) || userDetails[1].equals(email)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            // Info: No user file found, which is fine for first time run
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean authenticateUser(String userInput, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split(",");
                if ((userDetails[0].equals(userInput) || userDetails[1].equals(userInput))
                        && userDetails[2].equals(password)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            showAlert("Error", "User file not found. Please register.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}