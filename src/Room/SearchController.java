package Room;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox boxBuilding;
    @FXML private ChoiceBox boxCapacity;
    @FXML private ChoiceBox boxAV;
    @FXML private ChoiceBox boxSeating;
    @FXML private ChoiceBox startTime;
    @FXML private ChoiceBox endTime;
    @FXML private Label lblLogout;
    @FXML private TableView tblRes;
    @FXML private Button btnReserve;
    @FXML private Button btnSearch;
    @FXML private Button goBack;


    private ArrayList<String> timeList;

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
        tblRes.setEditable(false);
        tblRes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        /**
         * The following code came from this link:
         * https://stackoverflow.com/questions/41001703/how-to-customize-datepicker-in-javafx-so-that-i-can-restrict-the-user-to-not-be
         * This code is used to prevent a user from searching for a room to reserve in the past.
         */
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        datePicker.setEditable(false);
        timeList = new ArrayList<>();
        StringBuilder s;

        for (int i = 8; i <= 18; i++) {
            s = new StringBuilder();
            if (i<10){
                s.append("0");
                s.append(i);
                s.append(":00");
            }
            if (i == 11 || i == 10 || i == 12){
                s.append(i);
                s.append(":00");
            }
            if (i > 12){
                s.append("0");
                s.append(i-12);
                s.append(":00");
            }
            timeList.add(s.toString());
        }
        startTime.setItems(FXCollections.observableArrayList(timeList));
        startTime.getSelectionModel().selectFirst();
        endTime.setItems(FXCollections.observableArrayList(populateEndTimes(startTime.getSelectionModel().getSelectedItem().toString())));

        ArrayList<String> buildings = new ArrayList<>();
        buildings.add("Jepson");
        buildings.add("HCC");
        buildings.add("Trinkle");
        buildings.add("UC");
        boxBuilding.setItems((FXCollections.observableArrayList(buildings)));

        ArrayList<String> seating = new ArrayList<>();
        seating.add("Desks");
        seating.add("Chairs");
        boxSeating.setItems(FXCollections.observableArrayList(seating));

        ArrayList<String> AV = new ArrayList<>();
        AV.add("Audiovisual Equipment");
        AV.add("No Audiovisual Equipment");
        boxAV.setItems(FXCollections.observableArrayList(AV));

        ArrayList<String> size = new ArrayList<>();
        size.add("10 people");
        size.add("50 people");
        size.add("100 people");
        boxCapacity.setItems(FXCollections.observableArrayList(size));

        btnReserve.setDisable(true);
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

    /**
     * A method to populate the end times options based on what start time the user selects from the drop down
     * menu.
     * @param startTime the time the user selects to start their reservation
     */
    private ArrayList<String> populateEndTimes(String startTime){
        ArrayList<String> endTimes = new ArrayList<>();
        if(timeList.contains(startTime)){
            for(int i = timeList.indexOf(startTime)+1; i < timeList.size(); i++){
                endTimes.add(timeList.get(i));
            }
            endTimes.add("07:00");
            System.out.println(endTimes);
        }
        return endTimes;
    }

    /**
     * A method that returns to system's user to their appropriate original logged in screen based on their
     * privilege level.
     * @param event a mouse click on the button
     */
    @FXML
    private void goBack(ActionEvent event) {
        tblRes.getItems().clear();
        datePicker.getEditor().clear();
        switch (mainClass.user.getPriv()) {
            case ("Student"):
                myController.setScreen(MainRoom.screen4ID);
                break;
            case ("Faculty"):
                myController.setScreen(MainRoom.screen6ID);
                break;
            case ("Staff"):
                myController.setScreen(MainRoom.screen6ID);
                break;
            case ("Admin"):
                myController.setScreen(MainRoom.screen5ID);
                break;
        }
    }
}



