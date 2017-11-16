package Room;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.*;
import java.lang.*;

/**
 *
 * @author Whitney
 *  This class is used to manage the GUI elements and logic when a user wants to search for a reservation
 *  or manage their current reservations.
 */
public class LoggedInAdController implements Initializable, ControlledScreen {

    //private static LoggedInAdController instance;
    @FXML
    private Button btnMain;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnManage;
    @FXML
    private Button btnDeleteUsers;
    private MainRoom mainClass;
    @FXML
    public Label lblName;
    @FXML
    private Label lblLogOut;

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
     * Returns to the main screen
     * @param event when the button is clicked the main screen loads.
     */
    @FXML
    private void goToMain(ActionEvent event){
        myController.setScreen(MainRoom.screen1ID);
    }

    public void setName(String name){
        lblName.setText("Welcome " + name);
    }

    @FXML
    private void growText(MouseEvent event){
        lblLogOut.setFont(Font.font("System", FontWeight.BOLD, 24));
    }
    @FXML
    private void shrinkText(MouseEvent event){
        lblLogOut.setFont(Font.font("System" , FontWeight.BOLD, 18));
    }

    @FXML
    private void goToLoggedOut(MouseEvent event){
        mainClass.user = null;
        lblName.setText(" ");
        myController.setScreen(MainRoom.screen1ID); }

    @FXML
    private void goToDeleteUsers(ActionEvent event){
        myController.setScreen(MainRoom.screen11ID);
    }

     @FXML
    private void goToSearch(ActionEvent event){
        myController.setScreen(MainRoom.screen7ID);
    }
    @FXML
    private void goToManage(ActionEvent event){
        myController.setScreen(MainRoom.screen10ID);
    }


}


