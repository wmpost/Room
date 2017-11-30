package Room;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.control.Alert.*;
import javafx.util.Pair;
import sun.applet.Main;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 * @author Whitney
 *  This class is used to manage the GUI elements and logic of the main screen a user of
 *  the system will see. There is a button to log into the system, to create a new account
 *  and to go the screen to view the current reservations.
 */

public class MainController implements Initializable, ControlledScreen {

    private MainRoom mainClass;
    @FXML
    private Button btnCalendar;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnLogin;
    //private LoggedInAdController loggedinad;

    /**
     * Initializes the controller class.
     */
    ScreensController myController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mainClass = MainRoom.getInstance();
    }

    /**
     * Adds the class to the ScreensController.
     */
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }
    /**
     * A method that when you click on the button you will go to the calendar of reservations
     * screen.
     * @param event Takes a button click to go to the calendar screen.
     */
    @FXML
    private void goToCalendar(ActionEvent event){
        myController.setScreen(MainRoom.screen2ID);
    }

    @FXML
    private void goToRegister(ActionEvent event){
        mainClass.registerController.clearFields();
        myController.setScreen(MainRoom.screen3ID);
    }
    /**
     * This method was used to make popup appear for logging into the system with a user id and password
     * The code was taken from the following source:
     * http://code.makery.ch/blog/javafx-dialogs-official/
     * The method was modified to call the method needed to process information.
     * @param event the user clicks on the login button
     */
    @FXML
    private void showLogin(ActionEvent event){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login to UMW Room Reservation System");
        //dialog.setHeaderText("Please enter your username and password.");

// Set the icon (must be included in the project).
//        dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

// Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

// Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

// Do some validation (using the Java 8 lambda syntax).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

// Request focus on the username field by default.
        Platform.runLater(() -> username.requestFocus());

// Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        //Optional<Pair<String, String>> result = dialog.showAndWait();
        makeUser(dialog.showAndWait());
        /*result.ifPresent(usernamePassword -> {
            if(mainClass.database.checkPassword(usernamePassword.getKey(), usernamePassword.getValue()) == false){
                System.out.println("false");
                mainClass.showAlert("ERROR", "ERROR!", "Your username or password is invalid.");
                }
            else{
                String[] currentUser;
                currentUser = mainClass.database.getUser(usernamePassword.getKey());
                for(int i = 0; i<currentUser.length; i++){
                    System.out.println(currentUser[i]);
                }
                if(currentUser[0] != "NULL") {
                    mainClass.user = new User(currentUser[0],currentUser{1],currentUser[2], currentUser[3]);
                }
            }
            //return;

            //System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });
        */
    }

    /**
     * This method checks the input in the database and creates the user object in the main class.
     * @param u Optional String Pair containing the user name and password provided in the dialog box.
     */
    private void makeUser(Optional<Pair<String, String>> u){
        u.ifPresent(usernamePassword -> {
            //if (mainClass.database.test().equals(usernamePassword.getValue()))
            //   System.out.println("DB plain txt password equals input");
            System.out.println(mainClass.database.checkPassword("x", "x"));
            if (!mainClass.database.checkPassword(usernamePassword.getKey(), usernamePassword.getValue())){
                mainClass.showAlert("Error", "ERROR!", "Your username or password is invalid.");
            }
            else{
                System.out.println("Valid User");
                String[] currentUser;
                currentUser = mainClass.database.getUser(usernamePassword.getKey());
                if(currentUser[0] != "NULL") {
                    mainClass.user = new User(currentUser[0],currentUser[1],currentUser[2], currentUser[3]);
                    System.out.println(mainClass.user.getPriv());
                    goToLogin(mainClass.user);
                }
            }
        });

    }

    /**
     * A method to log the user into the system when they type in their username and password.
     * The method will take them to the appropriate screen based on their userlevel of student, faculty/staff
     * or admin.
     * @param u User
     */
    public void goToLogin(User u){
        switch(u.getPriv()){
            case("Student"):
                mainClass.loggedInController.setName(u.getFname());
                myController.setScreen(MainRoom.screen4ID);
                break;
            case("Faculty"):
                mainClass.loggedInFSController.setName(u.getFname());
                myController.setScreen(MainRoom.screen6ID);
                break;
            case("Staff"):
                mainClass.loggedInFSController.setName(u.getFname());
                myController.setScreen(MainRoom.screen6ID);
                break;
            case("Admin"):
                System.out.println(u.getFname());
                mainClass.loggedInAdController.setName(u.getFname());
                myController.setScreen(MainRoom.screen5ID);
                break;

        }
    }

}
