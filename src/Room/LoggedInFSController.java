package Room;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.lang.*;

/**
 *
 * @author Whitney
 *  This class is used to manage the GUI elements and logic when an faculty/staff level user
 *  logs into the system and wants to search for a reservation or manage their/student current reservations.
 *  The class has several fxml elements including buttons and labels along with an
 *  instance of the main class.
 */
public class LoggedInFSController implements Initializable, ControlledScreen {

    @FXML private Button btnMain;
    @FXML private Button btnSearch;
    @FXML private Button btnManage;
    private MainRoom mainClass;
    @FXML private Button btnManageStudentRes;
    @FXML private Label lblLogOut;
    @FXML private Label lblName;
    @FXML private Label lblLikes;

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
     * A method that prints out the current system user's name
     * @param name the user of the system
     */
    public void setName(String name){
        lblName.setText("Welcome " + name);
    }
    /**
     * A method that logs the user out of the system and returns to the mainscreen.
     * @param event the label is clicked.
     */

    /**
     * A method that displays the number of reservations and likes the user has.
     * @param r the number of reservations, an integer
     * @param l the number of likes, an integer
     */
    public void setLikes(int r, int l){lblLikes.setText("You have " + r + " reservations with " + l + " likes.");}
    @FXML
    private void goToLoggedOut(MouseEvent event){
        mainClass.user = null;
        lblLogOut.setText(" ");
        myController.setScreen(MainRoom.screen1ID); }
    /**
     * A method that makes the logout text bigger when the mouse enters the bounds of the label
     * @param event the mouse hovers over the label
     */
    @FXML
    private void growText(MouseEvent event){
        lblLogOut.setFont(Font.font("System", FontWeight.BOLD, 24));
    }
    /**
     * A method that makes the logout text regular sized when the mouse exits the bounds of the label
     * @param event the mouse leaves the label
     */
    @FXML
    private void shrinkText(MouseEvent event){
        lblLogOut.setFont(Font.font("System" , FontWeight.BOLD, 18));
    }
    /**
     * A method that changes the screen fo the search GUI screen.
     * @param event the button is clicked.
     */
    @FXML
    private void goToSearch(ActionEvent event){myController.setScreen(MainRoom.screen7ID);}
    /**
     * A method that changes the screen to the Management GUI screen.
     * @param event the button is clicked.
     */
    @FXML private void goToReservations(ActionEvent event){
        myController.setScreen(MainRoom.screen10ID);
    }
}


