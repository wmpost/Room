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
        import javafx.scene.paint.Color;

        import javax.swing.*;
        import java.lang.*;

/**
 *
 * @author Whitney
 *  This class is used to manage the GUI elements and logic when a user wants to register for
 *  a new account with the system.
 */
public class RegisterController implements Initializable, ControlledScreen {

    @FXML
    private Button btnMain;
    private MainRoom mainClass;
    @FXML private Button btnRegister;
    @FXML protected ChoiceBox boxUserType;
    @FXML protected TextField txtUserName;
    @FXML protected Label lblUserId;
    @FXML protected Label lblPassword;
    @FXML protected TextField txtFName;
    @FXML protected TextField txtLName;
    @FXML protected PasswordField pwdPass1;
    @FXML protected PasswordField pwdPass2;

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
        ArrayList<String> userchoices = new ArrayList<>();
        userchoices.add("Student");
        userchoices.add("Faculty");
        userchoices.add("Staff");
        userchoices.add("Admin");
        boxUserType.setItems(FXCollections.observableArrayList(userchoices));
        boxUserType.getSelectionModel().selectFirst();

        txtUserName.focusedProperty().addListener((obs, oldVal, newVal) ->
                checkUserName(newVal));
        pwdPass2.focusedProperty().addListener((obs, oldVal, newVal) ->
                checkPasswordMatch(newVal));
    }
    public void clearFields(){
        boxUserType.getSelectionModel().selectFirst();
        txtUserName.setText("");
        lblUserId.setText("");
        lblPassword.setText("");
        txtFName.setText("");
        txtLName.setText("");
        pwdPass1.setText("");
        pwdPass2.setText("");
    }
//a method to check if the username the user types in is available
    private void checkUserName(Boolean f){
        if (!f){
            if(txtUserName.getText().length() > 0){
                if(mainClass.database.isUserNameAvailable(txtUserName.getText())){
                    lblUserId.setTextFill(Color.GREEN);
                    lblUserId.setText("Username Available");
                }
                else{
                    lblUserId.setTextFill(Color.RED);
                    lblUserId.setText("Username NOT available");
                }
            }
            else{
                lblUserId.setText("");
            }
        }
    }

    //a method to check if the passwords match
    private void checkPasswordMatch(Boolean f) {
        if (!f) {
            if (!pwdPass1.getText().equals(pwdPass2.getText())) {
                lblPassword.setTextFill(Color.RED);
                lblPassword.setText("Your passwords do not match!");
            }
            else {
                lblPassword.setText("");
            }
        }
    }

    private void Register(){
        if(txtUserName.getText().length() == 0 || txtFName.getText().length() == 0 ||
                txtLName.getText().length() == 0 || pwdPass1.getText().length() == 0 ||
                pwdPass2.getText().length() == 0 || !pwdPass1.getText().equals(pwdPass2.getText())
                || !mainClass.database.isUserNameAvailable(txtUserName.getText()))
        {
            mainClass.showAlert("Error", "Something is missing or wrong!", "One of your" +
                    " data fields is blank or your passwords don't match or your username is taken, please fix this!");
        }
        else{
            boolean result;
            System.out.println(pwdPass1.getText());
            result = mainClass.database.addUser(txtUserName.getText(),txtFName.getText(),txtLName.getText(),pwdPass1.getText(),boxUserType.getValue().toString());
            if(result){
                mainClass.showAlert("Info", "Success!", "Your account has been registered, please log in" +
                        "to search for and make room reservations!");
                txtUserName.clear();
                txtLName.clear();
                txtFName.clear();
                pwdPass1.clear();
                pwdPass2.clear();
                myController.setScreen(MainRoom.screen1ID);
            }
            else mainClass.showAlert("Error", "Error", "There was something wrong with your " +
                    "input please try again.");
        }
    }
    @FXML
    public void registerButton(ActionEvent event){
        Register();
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
        clearFields();
        myController.setScreen(MainRoom.screen1ID);
    }


}

