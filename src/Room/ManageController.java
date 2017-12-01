package Room;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Whitney
 * The ManageController.java class is used to manage and control all of the GUI front end for a user logged into
 * the system who is on the screen to view the reservatio calendar, like or delete reservations. The class has
 * an instance of the main class, several FXML components including a date picker, labels, buttons, image view,
 * check box, and a table view. It also has an observable list of LikedReservations which are used to populate
 * the table view for the user to select from.
 */

public class ManageController implements Initializable, ControlledScreen {

    @FXML
    private Label lblLogout;
    @FXML
    private Button btnReturn;
    @FXML
    private Button btnDelRes;
    @FXML
    private TableView tblRes;
    @FXML
    private DatePicker dpCal;
    @FXML
    private ImageView likebtn;
    @FXML
    private CheckBox cbDeletes;

    private MainRoom mainClass;
    private ObservableList<LikedReservation> resList;
    private TableColumn timeStart = new TableColumn("Start Time");
    private TableColumn timeEnd = new TableColumn("End Time");
    private TableColumn building = new TableColumn("Building");
    private TableColumn room = new TableColumn("Room");
    private TableColumn name = new TableColumn("Name");
    private TableColumn likes = new TableColumn("Likes");


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
        tblRes.setEditable(false);
        tblRes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblRes.getColumns().addAll(name, timeStart, timeEnd, building, room, likes);
        btnDelRes.setDisable(true);
    }

    /**
     * Adds the class to the ScreensController.
     */
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    /**
     * A method that repopulates the table view with the most up to date database information.
     */
    private void refreshTable() {
        resList = mainClass.database.getAllReservationsLiked(dpCal.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        tblRes.setItems(resList);
        timeStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        timeEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        building.setCellValueFactory(new PropertyValueFactory<>("building"));
        room.setCellValueFactory(new PropertyValueFactory<>("room"));
        name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        likes.setCellValueFactory(new PropertyValueFactory<>("likes"));
    }

    /**
     * The FXML linked method that calls the repopulation of the table view.
     * @param event a mouse click on the check box.
     */
    @FXML
    void fillTable(ActionEvent event) {
        refreshTable();
    }

    /**
     * A method that calls the addLikes method in the database class and displays a popup with the number
     * of likes they made when clicking the like image view.
     * @param event
     */
    @FXML
    private void LikeButton(MouseEvent event) {
        int likes = 0;
        likes = mainClass.database.addLikes(mainClass.user.getName(), tblRes.getSelectionModel().getSelectedItems());
        refreshTable();
        mainClass.showAlert("Info", "Likes", "You liked " + likes + " reservations!");
    }

    /**
     * A method that makes the logout text bigger when the mouse enters the bounds of the label
     * @param event the mouse hovers over the label
     */
    @FXML
    private void growText(MouseEvent event) {
        lblLogout.setFont(Font.font("System", FontWeight.BOLD, 24));
    }

    /**
     * A method that makes the logout text regular sized when the mouse exits the bounds of the label
     * @param event the mouse leaves the label
     */
    @FXML
    private void shrinkText(MouseEvent event) {
        lblLogout.setFont(Font.font("System", FontWeight.BOLD, 18));
    }

    /**
     * A method that logs the user out of the system and returns to the mainscreen.
     * @param event the label is clicked.
     */
    @FXML
    private void goToLoggedOut(MouseEvent event) {
        mainClass.user = null;
        //lblName.setText(" ");
        myController.setScreen(MainRoom.screen1ID);
    }
    /**
     * A method that returns to system's user to their appropriate original logged in screen based on their
     * privilege level.
     * @param event a mouse click on the button
     */
    @FXML
    private void goBack(ActionEvent event) {
        tblRes.getItems().clear();
        dpCal.getEditor().clear();
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
    /**
     * A method that calls the database class in order to populate reservations that can be deleted
     * onto the table view. Enables the button that will actually delete the reservations.
     * @param event
     */
    @FXML private void deleteRes(ActionEvent event){
        if(cbDeletes.isSelected()){
            if(dpCal.getValue() != null){
                resList = mainClass.database.deletableReservations(dpCal.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),mainClass.user.getName());
                tblRes.setItems(resList);
                timeStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
                timeEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
                building.setCellValueFactory(new PropertyValueFactory<>("building"));
                room.setCellValueFactory(new PropertyValueFactory<>("room"));
                name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
                likes.setCellValueFactory(new PropertyValueFactory<>("likes"));
                btnDelRes.setDisable(false);
            }
            else {
                cbDeletes.setSelected(false);
                mainClass.showAlert("Error","Error","Please select a date first, then click on the check box to enable the ability to delete reservations.");
            }

        }
        else{
            refreshTable();
            btnDelRes.setDisable(true);
        }
    }

    @FXML private void DeleteButtonAction(ActionEvent event)
        {
            ObservableList<LikedReservation> deleteRes = tblRes.getSelectionModel().getSelectedItems();
            if(!deleteRes.isEmpty()){
                //code for this alert box comes from http://code.makery.ch/blog/javafx-dialogs-official/
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion of Reservations");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete these reservations from the system?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    System.out.println(mainClass.database.deleteReservation(tblRes.getSelectionModel().getSelectedItems()));
                    cbDeletes.setSelected(false);
                    refreshTable();
                } else {
                    tblRes.getSelectionModel().clearSelection();
                }
            }
        }


}
