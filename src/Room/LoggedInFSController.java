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
 *  This class is used to manage the GUI elements and logic when a user wants to search for a reservation
 *  or manage their current reservations.
 */
public class LoggedInFSController implements Initializable, ControlledScreen {

    @FXML private Button btnMain;
    @FXML private Button btnSearch;
    @FXML private Button btnManage;
    private MainRoom mainClass;
    @FXML private Button btnManageStudentRes;
    @FXML private Label lblLogOut;
    @FXML private Label lblName;

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

    public void setName(String name){
        lblName.setText("Welcome " + name);
    }

    @FXML
    private void goToLoggedOut(MouseEvent event){
        mainClass.user = null;
        lblLogOut.setText(" ");
        myController.setScreen(MainRoom.screen1ID); }

    @FXML
    private void growText(MouseEvent event){
        lblLogOut.setFont(Font.font("System", FontWeight.BOLD, 24));
    }
    @FXML
    private void shrinkText(MouseEvent event){
        lblLogOut.setFont(Font.font("System" , FontWeight.BOLD, 18));
    }

    @FXML
    private void goToSearch(ActionEvent event){myController.setScreen(MainRoom.screen7ID);}

    @FXML private void goToReservations(ActionEvent event){
        myController.setScreen(MainRoom.screen10ID);
    }
}


