package Room;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserManageController implements Initializable, ControlledScreen {

    @FXML private Label lblLogout;
    @FXML private Button btnDelRes;
    @FXML private TableView tblRes;
    @FXML private Button btnReturn;
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
    private void goToLoggedIn(ActionEvent event){
        myController.setScreen(MainRoom.screen4ID);
    }

    @FXML
    private void goToLoggedOut(MouseEvent event){
        mainClass.user = null;
        lblLogout.setText(" ");
        myController.setScreen(MainRoom.screen1ID); }

    @FXML
    private void growText(MouseEvent event){
        lblLogout.setFont(Font.font("System", FontWeight.BOLD, 24));
    }
    @FXML
    private void shrinkText(MouseEvent event){
        lblLogout.setFont(Font.font("System" , FontWeight.BOLD, 18));
    }
    /*@FXML
    private void deleteReservation(ActionEvent event){

    }*/
}
