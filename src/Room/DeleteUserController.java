package Room;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.Optional;
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
    private TableColumn Username = new TableColumn("Username");
    private TableColumn FirstName = new TableColumn("First Name");
    private TableColumn LastName = new TableColumn("Last Name");
    private TableColumn UserLevel = new TableColumn("User Level");

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

        Username.setMinWidth(75);
        FirstName.setMinWidth(75);
        LastName.setMinWidth(75);
        UserLevel.setMinWidth(75);
        TblofUsers.getColumns().addAll(Username, FirstName, LastName, UserLevel);
        TblofUsers.setEditable(false);
        TblofUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        TblofUsers.getSelectionModel().clearSelection();
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
    public void fillTable(String u) {
        ObservableList<User> userList = mainClass.database.getUsersToDelete(u);
        TblofUsers.setItems(userList);
        Username.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        FirstName.setCellValueFactory(new PropertyValueFactory<User, String>("fname"));
        LastName.setCellValueFactory(new PropertyValueFactory<User, String>("lname"));
        UserLevel.setCellValueFactory(new PropertyValueFactory<User, String>("priv"));
    }
    @FXML
    private void deleteUser(ActionEvent event){
        ObservableList<User> deleteUsers = TblofUsers.getSelectionModel().getSelectedItems();
        if(!deleteUsers.isEmpty()){
            //code for this alert box comes from http://code.makery.ch/blog/javafx-dialogs-official/
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion of Users");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete these users from the system?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                System.out.println(mainClass.database.deleteUsers(TblofUsers.getSelectionModel().getSelectedItems()));
                fillTable(mainClass.user.getName());
            } else {
                TblofUsers.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    private void goToLoggedOut(MouseEvent event){
        mainClass.user = null;
        myController.setScreen(MainRoom.screen1ID); }

}


