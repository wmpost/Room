package Room;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

import javax.swing.*;
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
    private ObservableList<ResOptions> resOps;
    private TableColumn timeStart = new TableColumn("Start Time");
    private TableColumn timeEnd = new TableColumn("End Time");
    private TableColumn building = new TableColumn("Building");
    private TableColumn room = new TableColumn("Room");
    private TableColumn capacity = new TableColumn("Capacity");
    private TableColumn av = new TableColumn("Audiovisual Options");
    private TableColumn seat = new TableColumn("Desks");
    private TableColumn date = new TableColumn("Date");


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
        tblRes.getSelectionModel().selectedItemProperty().addListener((obs, os, ns) -> {
            if(ns != null){
                btnReserve.setDisable(false);
            }
        });
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
        /**
         * The following override method comes from the website below:
         * https://stackoverflow.com/questions/14522680/javafx-choicebox-events
         */
        startTime.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                endTime.setItems(FXCollections.observableArrayList(
                        populateEndTimes(startTime.getItems().get((Integer) newValue).toString())));
            }
        });

        startTime.setItems(FXCollections.observableArrayList(timeList));
        //endTime.setItems(FXCollections.observableArrayList(populateEndTimes(startTime.getSelectionModel().getSelectedItem().toString())));

        ArrayList<String> buildings = new ArrayList<>();
        buildings.add("Jepson");
        buildings.add("HCC");
        buildings.add("Trinkle");
        buildings.add("UC");
        buildings.add("Any");
        boxBuilding.setItems((FXCollections.observableArrayList(buildings)));
        boxBuilding.getSelectionModel().selectLast();

        ArrayList<String> seating = new ArrayList<>();
        seating.add("Desks");
        seating.add("Tables");
        seating.add("Any");
        boxSeating.setItems(FXCollections.observableArrayList(seating));
        boxSeating.getSelectionModel().selectLast();

        ArrayList<String> AV = new ArrayList<>();
        AV.add("Audiovisual Equipment");
        AV.add("No Audiovisual Equipment");
        AV.add("Any");
        boxAV.setItems(FXCollections.observableArrayList(AV));
        boxAV.getSelectionModel().selectLast();

        ArrayList<String> size = new ArrayList<>();
        size.add("10");
        size.add("50");
        size.add("100");
        size.add("Any");
        boxCapacity.setItems(FXCollections.observableArrayList(size));
        boxCapacity.getSelectionModel().selectLast();

        btnReserve.setDisable(true);
        tblRes.setEditable(false);
        tblRes.getColumns().addAll(date, timeStart, timeEnd, building, room, capacity, seat, av);
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
        tblRes.getItems().clear();
        datePicker.getEditor().clear();
        datePicker.setValue(null);
        startTime.getSelectionModel().selectFirst();
        endTime.getSelectionModel().selectFirst();
        boxBuilding.getSelectionModel().selectLast();
        boxSeating.getSelectionModel().selectLast();
        boxAV.getSelectionModel().selectLast();
        boxCapacity.getSelectionModel().selectLast();
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
        datePicker.setValue(null);
        startTime.getSelectionModel().select(0);
        endTime.getSelectionModel().select(0);
        boxCapacity.getSelectionModel().selectLast();
        boxAV.getSelectionModel().selectLast();
        boxSeating.getSelectionModel().selectLast();
        boxBuilding.getSelectionModel().selectLast();
        switch (mainClass.user.getPriv()) {
            case ("Student"):
                Pair<Integer, Integer> p = mainClass.database.likeResCount(mainClass.user.getName());
                mainClass.loggedInController.setLikes(p.getKey(), p.getValue());
                myController.setScreen(MainRoom.screen4ID);
                break;
            case ("Faculty"):
                Pair<Integer, Integer> q = mainClass.database.likeResCount(mainClass.user.getName());
                mainClass.loggedInFSController.setLikes(q.getKey(), q.getValue());;
                myController.setScreen(MainRoom.screen6ID);
                break;
            case ("Staff"):
                Pair<Integer, Integer> r = mainClass.database.likeResCount(mainClass.user.getName());
                mainClass.loggedInFSController.setLikes(r.getKey(), r.getValue());
                myController.setScreen(MainRoom.screen6ID);
                break;
            case ("Admin"):
                Pair<Integer, Integer> s = mainClass.database.likeResCount(mainClass.user.getName());
                mainClass.loggedInAdController.setLikes(s.getKey(), s.getValue());
                myController.setScreen(MainRoom.screen5ID);
                break;
        }
    }
    private String fixTime(String time){
        String fixedTime;
        switch(time){
            case "01:00":
                fixedTime = "13:00:00";
                break;
            case "02:00":
                fixedTime = "14:00:00";
                break;
            case "03:00":
                fixedTime = "15:00:00";
                break;
            case "04:00":
                fixedTime = "16:00:00";
                break;
            case "05:00":
                fixedTime = "17:00:00";
                break;
            case "06:00":
                fixedTime = "18:00:00";
                break;
            case "07:00":
                fixedTime = "19:00:00";
                break;
            default:
                fixedTime = time + ":00";
        }
        return fixedTime;
    }
    @FXML public void searchForRooms(ActionEvent event) {
        if (datePicker.getValue() != null && startTime.getSelectionModel().getSelectedIndex() >-1 && endTime.getSelectionModel().getSelectedIndex() >-1){
            String d = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            resOps = mainClass.database.showReserveOptions(mainClass.user.getName(), d, boxBuilding.getSelectionModel().getSelectedItem().toString(),
                    boxCapacity.getSelectionModel().getSelectedItem().toString(), boxAV.getSelectionModel().getSelectedItem().toString(),
                    boxSeating.getSelectionModel().getSelectedItem().toString(), fixTime(startTime.getSelectionModel().getSelectedItem().toString()),
                    fixTime(endTime.getSelectionModel().getSelectedItem().toString()));
            tblRes.setItems(resOps);
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            timeStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            timeEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            building.setCellValueFactory(new PropertyValueFactory<>("building"));
            room.setCellValueFactory(new PropertyValueFactory<>("room"));
            capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
            av.setCellValueFactory(new PropertyValueFactory<>("av"));
            seat.setCellValueFactory(new PropertyValueFactory<>("seating"));
        }
        else mainClass.showAlert("Error", "Error", "Please make sure to at minimum select a date, start and end time in order to search for a room!");
    }

    /**
     * A method that returns the user's reservation and like count
     * @param event
     */
    @FXML public void reserveRoom(ActionEvent event){
        int reservations = mainClass.database.addReservation(tblRes.getSelectionModel().getSelectedItems());
        mainClass.showAlert("Info", "Reservations", "You have reserved "+reservations+" room");
        btnReserve.setDisable(true);
        searchForRooms(new ActionEvent());
    }

    }



