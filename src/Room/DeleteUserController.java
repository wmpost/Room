package Room;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Shawn
 *  This class is used to manage the GUI elements and logic when an administrator level user wants to search for a reservation
 *  or manage their current reservations.
 */
public class DeleteUserController implements Initializable, ControlledScreen {

    @FXML
    private Button btnReturn;
    @FXML
    private Button btnDeleteUser;
    @FXML
    private Label lblLogOut;
    @FXML
    private TableView TblofUsers = new TableView();
    private TableColumn Username;
    private TableColumn FirstName;
    private TableColumn LastName;
    private TableColumn UserLevel;

    private MainRoom mainClass;

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
    private void goToLoggedInAD(ActionEvent event){
        myController.setScreen(MainRoom.screen5ID);
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
        //lblName.setText(" ");
        myController.setScreen(MainRoom.screen1ID); }

}


