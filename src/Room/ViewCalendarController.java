package Room;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;

import java.lang.*;

/**
 *
 * @author Whitney
 *  This class is used to manage the GUI elements and logic when a guest user wants to view the
 *  current reservation Calendar. The user can select the date they want to display the
 *  reservations for and the GUI will update to display the information from the
 *  database. The class contains several fxml elements inclyding a date picker, buttons, and a table view.
 *  It also has an instance of the main class.
 */
public class ViewCalendarController implements Initializable, ControlledScreen {

    @FXML
    private Button btnMain;
    @FXML
    private DatePicker dpCal;
    @FXML
    private TableView tblRes = new TableView();
    private MainRoom mainClass;
    private ObservableList<Reservation> resList;
    private TableColumn timeStart = new TableColumn("Start Time");
    private TableColumn timeEnd = new TableColumn("End Time");
    private TableColumn building = new TableColumn("Building");
    private TableColumn room = new TableColumn("Room");

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
        timeStart.setMinWidth(77);
        timeEnd.setMinWidth(77);
        building.setMinWidth(157);
        room.setMinWidth(86);

        tblRes.setEditable(false);
        tblRes.getColumns().addAll(timeStart, timeEnd, building, room);
    }

    /**
     * Adds the class to the ScreensController.
     */
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    /**
     * A method that returns to the main GUI screen
     * @param event when the mouse clicks the return button
     */
    @FXML
    private void goToMain(ActionEvent event){
        tblRes.getItems().clear();
        myController.setScreen(MainRoom.screen1ID);
    }

    /**
     * A method that fills the table with the reservations for the date that the user selects
     * @param event when the user selects a date with the fxml date picker.
     */
    @FXML void fillTable(ActionEvent event) {
        resList = mainClass.database.getAllReservations(dpCal.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        tblRes.setItems(resList);
        timeStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        timeEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        building.setCellValueFactory(new PropertyValueFactory<>("building"));
        room.setCellValueFactory(new PropertyValueFactory<>("room"));
    }
}
