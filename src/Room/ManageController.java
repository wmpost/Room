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
import java.util.ResourceBundle;

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
    }

    /**
     * Adds the class to the ScreensController.
     */
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

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

    @FXML
    void fillTable(ActionEvent event) {
        refreshTable();
    }

    @FXML
    private void LikeButton(MouseEvent event) {
        int likes = 0;
        likes = mainClass.database.addLikes(mainClass.user.getName(), tblRes.getSelectionModel().getSelectedItems());
        refreshTable();
        mainClass.showAlert("Info", "Likes", "You liked " + likes + " reservations!");
    }

    @FXML
    private void growText(MouseEvent event) {
        lblLogout.setFont(Font.font("System", FontWeight.BOLD, 24));
    }

    @FXML
    private void shrinkText(MouseEvent event) {
        lblLogout.setFont(Font.font("System", FontWeight.BOLD, 18));
    }


    @FXML
    private void goToLoggedOut(MouseEvent event) {
        mainClass.user = null;
        //lblName.setText(" ");
        myController.setScreen(MainRoom.screen1ID);
    }

    @FXML
    private void goBack(ActionEvent event) {
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
