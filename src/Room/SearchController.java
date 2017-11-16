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
 *  This class is used to manage the GUI elements and logic when a user wants to register for
 *  a new account with the system.
 */
public class SearchController implements Initializable, ControlledScreen {

    @FXML
    private Button btnMain;
    private MainRoom mainClass;
    @FXML private ChoiceBox boxBuilding;
    @FXML private ChoiceBox boxCapacity;
    @FXML private ChoiceBox boxAV;
    @FXML private ChoiceBox boxSeating;
    @FXML private Label lblLogout;

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
        ArrayList<String> Buildings = new ArrayList<>();
        Buildings.add("Jepson Hall");
        Buildings.add("Hurley Convergence Center (HCC)");
        Buildings.add("Trinkle Hall");
        Buildings.add("University Center");
        boxBuilding.setItems(FXCollections.observableArrayList(Buildings));
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

    private void logOut(){
        mainClass.user = null;
        lblLogout.setText(" ");
    }
    @FXML
    private void growText(MouseEvent event){
        lblLogout.setFont(Font.font("System", FontWeight.BOLD, 24));
    }
    @FXML
    private void shrinkText(MouseEvent event){
        lblLogout.setFont(Font.font("System" , FontWeight.BOLD, 18));
    }


    @FXML
    private void goToLoggedOut(MouseEvent event){
        mainClass.user = null;
        //lblName.setText(" ");
        myController.setScreen(MainRoom.screen1ID); }

        @FXML
    private void goToLogin(ActionEvent event) {
        findUserLevel(mainClass.user);
        }

    private void findUserLevel(User u){
            mainClass.mainController.goToLogin(u);
                }
}



