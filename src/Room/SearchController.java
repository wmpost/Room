package Room;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 *  This class is used to manage the GUI elements and logic when a user wants to search for a room to reserve
 *  within the system. It has several private data members for the GUI elemenets inlcuding ChoiceBoxes for making
 *  selections from, a button to search with and a logout Label. It also has an instance of MainRoom called mainClass
 */
public class SearchController implements Initializable, ControlledScreen {

    @FXML
    private MainRoom mainClass;
    @FXML private ChoiceBox boxBuilding;
    @FXML private ChoiceBox boxCapacity;
    @FXML private ChoiceBox boxAV;
    @FXML private ChoiceBox boxSeating;
    @FXML private Label lblLogout;
    @FXML private TableView tblRes;

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
     * A method to log the user out of the system.
     */
    private void logOut(){
        mainClass.user = null;
        lblLogout.setText(" ");
    }

    /**
     * A method to make the logout text label bigger when the user hovers over it.
     * @param event the mouse moves over the logout label
     */
    @FXML
    private void growText(MouseEvent event){
        lblLogout.setFont(Font.font("System", FontWeight.BOLD, 24));
    }
    /**
     * A method to make the logout text label back to regular size when the user moves the mouse away from the label
     * @param event the mouse moves off from the logout label.
     */
    @FXML
    private void shrinkText(MouseEvent event){
        lblLogout.setFont(Font.font("System" , FontWeight.BOLD, 18));
    }

    /**
     * A method to return to the main GUI screen and log the user out of the system.
     * @param event a user clicks on the logout button
     */
    @FXML
    private void goToLoggedOut(MouseEvent event){
        mainClass.user = null;
        //lblName.setText(" ");
        myController.setScreen(MainRoom.screen1ID); }

    /**
     * A method to go back to the logged in screen based on their user level
     * @param event a mouse click on the button to go back
     */
        @FXML
    private void goToLogin(ActionEvent event) {
        findUserLevel(mainClass.user);
        }

    /**
     * A method to find out the user level of the user currently logged into the system
     * @param u A String of the user
     */
    private void findUserLevel(User u){
            mainClass.mainController.goToLogin(u);
                }
}



