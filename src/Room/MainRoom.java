package Room;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Alert.*;

import java.lang.String;

import DB.*;

/**
 * @author Whitney
 * The main class of the program, this displays the GUI elements and moves the user of the system
 * from screen to screen based on their input.
 * This class has many data members including strings to represent each of the other fxml/controller pairs
 * instances of these controllers, a User called user and a Stage called stage for all of the GUI elements
 * to be displayed on.
 */

public class MainRoom extends Application {

    private static MainRoom instance;
    public DB database;

    public static String screen1ID = "main";
    public static String screen1File = "main.fxml";
    public static String screen2ID = "viewcal";
    public static String screen2File = "ViewCalendar.fxml";
    public static String screen3ID = "register";
    public static String screen3File = "Register.fxml";
    public static String screen4ID = "loggedin";
    public static String screen4File = "LoggedIn.fxml";
    public static String screen5File = "LoggedInAd.fxml";
    public static String screen5ID = "LoggedInAd";
    public static String screen6ID = "LoggedInFS";
    public static String screen6File = "LoggedInFS.fxml";
    public static String screen7File = "Search.fxml";
    public static String screen7ID = "Search";
    public static String screen10ID = "Manage";
    public static String screen10File = "Manage.fxml";
    public static String screen11File = "DeleteUser.fxml";
    public static String screen11ID = "DeleteUser";
    public Image image;


    protected LoggedInAdController loggedInAdController;
    protected LoggedInFSController loggedInFSController;
    protected LoggedInController loggedInController;
    protected MainController mainController;
    protected RegisterController registerController;
    protected SearchController searchController;
    protected ViewCalendarController viewCalendarController;
    protected ManageController manageController;
    protected DeleteUserController deleteUserController;

    public User user;

    private Stage stage;

    /**
     * Calls default constructor to initialize main.
     */
    public MainRoom() {
        instance = this;
        image = new Image("Resources/icon.jpg");
    }

    /**
     * A method for the controller classes to get an instance of the main class to use main methods.
     *
     * @return the instance of the main class.
     */
    public static MainRoom getInstance() {
        return instance;
    }

    /**
     * The start method is used instead of the public static main.
     */
    @Override
    public void start(Stage stage) throws Exception {
        database = new DB();
        //the stage is set to display the fxml files
        this.stage = stage;
        //the GUI elements are started
        ScreensController mainContainer = new ScreensController();
        mainContainer.loadScreen(MainRoom.screen1ID, MainRoom.screen1File);
        mainContainer.loadScreen(MainRoom.screen2ID, MainRoom.screen2File);
        mainContainer.loadScreen(MainRoom.screen3ID, MainRoom.screen3File);
        mainContainer.loadScreen(MainRoom.screen4ID, MainRoom.screen4File);
        mainContainer.loadScreen(MainRoom.screen5ID, MainRoom.screen5File);
        mainContainer.loadScreen(MainRoom.screen6ID, MainRoom.screen6File);
        mainContainer.loadScreen(MainRoom.screen7ID, MainRoom.screen7File);
        mainContainer.loadScreen(MainRoom.screen10ID, MainRoom.screen10File);
        mainContainer.loadScreen(MainRoom.screen11ID, MainRoom.screen11File);

        mainContainer.setScreen(MainRoom.screen1ID);

        Group root = new Group();
        root.getChildren().addAll(mainContainer);
        Scene scene = new Scene(root);
        stage.setTitle("UMW Room Reservation Software");
        stage.getIcons().add(image);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * A method which displays a popup alert
     * @param type the type of alert to be displayed, either an Error or Info
     * @param title String that is the title of the popup alert
     * @param message String of the message contained in the popup alert
     */
    public void showAlert (String type, String title, String message)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(image);
        switch (type) {
            case "Info":
                alert.setAlertType(AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
                break;
            case "Error":
                alert.setAlertType(AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
                break;
        }
    }
    /**
     * The main method 
     */
    public static void main (String[]args){
        launch(args);
    }
}
