package DB;

import java.sql.*;

import Room.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.h2.Driver;
import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * @author Whitney
 * The DB.java class purpose is to be the back end to the GUI, it stores all of the functions related to accessing,
 * modifying, and removing data stored in the H2 database where all of the data on users, buildings,
 * reservations and associated data is stored. The class has several private data members including a Connection
 * for accessing the database called conn, a ResultSet called rs for executing the queries, several prepared
 * statements associated with each method for running queries on the database and an instance of the password
 * management class called pwdMgr for hashing passwords that users enter in the GUI frontend.
 */

public class DB {

    private Connection conn = null;
    private ResultSet rs = null;
    private PreparedStatement userInsert = null;
    private String userInsertString;
    private PreparedStatement checkPassword = null;
    private String checkPasswordString;
    private PreparedStatement checkUserName = null;
    private String checkUserNameString;
    private PreparedStatement getUser = null;
    private String getUserString;
    private PreparedStatement getAllReservations = null;
    private String getAllReservationsString;
    private String getAllUsersString;
    private PreparedStatement getAllUsers = null;
    private  String deleteUserString;
    private  PreparedStatement deleteUsers;
    private String getAllReservationsLikedString;
    private PreparedStatement getAllReservationsLiked;
    private String addLikesString;
    private PreparedStatement addLikes;
    private String showDeletableString;
    private PreparedStatement showDeletable;
    private String deleteReservationString;
    private PreparedStatement deleteReservation;
    private String deleteStudentResString;
    private PreparedStatement deleteStudentRes;
    private Statement stmt;
    private String availableRoomPart1, availableRoomPart2, availRoomDate, availRoomDate2, availRoomStart,
            availRoomStart2, availRoomEnd, availRoomEnd2;
    private boolean availAddAND;
    private String addReservationString;
    private PreparedStatement addReservation;

    private PwdMgr pwdmgr;

    public DB() {
        userInsertString = "INSERT INTO USER(username, firstname, lastname, password, salt, userlevel) VALUES(?, ?, ?, ?, ?, ?);";
        checkPasswordString = "SELECT PASSWORD, SALT FROM USER WHERE USERNAME=?";
        checkUserNameString = "SELECT USERNAME FROM USER WHERE USERNAME=?";
        getUserString = "SELECT USERNAME, FIRSTNAME, LASTNAME, USERLEVEL FROM USER WHERE USERNAME=?";
        getAllReservationsString = "SELECT RESERVATION.*, LOCATION.BUILDING, LOCATION.ROOM FROM RESERVATION JOIN LOCATION ON RESERVATION.LOCATION = LOCATION.ID WHERE RESERVATION.STARTDATE=? ORDER BY STARTDATE, STARTTIME, LOCATION ";
        getAllUsersString ="SELECT USERNAME, FIRSTNAME, LASTNAME, USERLEVEL FROM USER WHERE USERNAME != ?";
        deleteUserString = "DELETE FROM USER WHERE USERNAME =?";
        getAllReservationsLikedString = "SELECT RESERVATION.*, LOCATION.BUILDING, LOCATION.ROOM, USER.FIRSTNAME, USER.LASTNAME, (SELECT COUNT(*) FROM LIKES WHERE LIKES.RESERVATION = RESERVATION.ID) AS LIKECOUNT FROM RESERVATION JOIN LOCATION ON RESERVATION.LOCATION = LOCATION.ID JOIN USER ON RESERVATION.USER = USER.USERNAME WHERE RESERVATION.STARTDATE=? ORDER BY STARTDATE, STARTTIME, LOCATION";
        addLikesString = "INSERT INTO LIKES (RESERVATION, USER) VALUES (?, ?)";
        showDeletableString = "SELECT RESERVATION.*, LOCATION.BUILDING, LOCATION.ROOM, USER.FIRSTNAME, USER.LASTNAME, (SELECT COUNT(*) FROM LIKES WHERE LIKES.RESERVATION = RESERVATION.ID) AS LIKECOUNT FROM RESERVATION JOIN LOCATION ON RESERVATION.LOCATION = LOCATION.ID JOIN USER ON RESERVATION.USER = USER.USERNAME WHERE RESERVATION.STARTDATE=? AND (USER.USERLEVEL = 'Student' OR USER.USERNAME = ?) ORDER BY STARTDATE, STARTTIME, LOCATION";
        deleteReservationString = "DELETE FROM RESERVATION WHERE ID =?";
        deleteStudentResString = "SELECT RESERVATION.*, LOCATION.BUILDING, LOCATION.ROOM, USER.FIRSTNAME, USER.LASTNAME, (SELECT COUNT(*) FROM LIKES WHERE LIKES.RESERVATION = RESERVATION.ID) AS LIKECOUNT FROM RESERVATION JOIN LOCATION ON RESERVATION.LOCATION = LOCATION.ID JOIN USER ON RESERVATION.USER = USER.USERNAME WHERE RESERVATION.STARTDATE=? AND USER.USERNAME = ? ORDER BY STARTDATE, STARTTIME, LOCATION";
        availableRoomPart1 = "SELECT * FROM LOCATION WHERE ";
        availableRoomPart2 = "ID NOT IN (SELECT LOCATION FROM RESERVATION WHERE ";
        availRoomDate = "STARTDATE = '";
        availRoomDate2 = "' AND (STARTTIME >= '";
        availRoomStart = "' AND STARTTIME < '";
        availRoomStart2 = "') OR (ENDTIME > '";
        availRoomEnd = "' AND ENDTIME <='";
        availRoomEnd2 = "'))";
        availAddAND = false;
        addReservationString = "INSERT INTO RESERVATION (STARTDATE, STARTTIME, ENDTIME, USER, LOCATION) VALUES (?,?,?,?,?)";

        pwdmgr = new PwdMgr();

        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.
                    getConnection("jdbc:h2:./src/DB/rooms", "sa", "");

            userInsert = conn.prepareStatement(userInsertString);
            checkPassword = conn.prepareStatement(checkPasswordString);
            checkUserName = conn.prepareStatement(checkUserNameString);
            getUser = conn.prepareStatement(getUserString);
            getAllReservations = conn.prepareStatement(getAllReservationsString);
            getAllUsers = conn.prepareStatement(getAllUsersString);
            deleteUsers = conn.prepareStatement(deleteUserString);
            getAllReservationsLiked = conn.prepareStatement(getAllReservationsLikedString);
            addLikes = conn.prepareStatement(addLikesString);
            showDeletable = conn.prepareStatement(showDeletableString);
            deleteReservation = conn.prepareStatement(deleteReservationString);
            deleteStudentRes = conn.prepareStatement(deleteStudentResString);
            stmt = conn.createStatement();
            addReservation = conn.prepareStatement(addReservationString);

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public ObservableList<ResOptions> showReserveOptions(String user, String date, String building, String capacity, String av, String seating, String starttime, String endtime){
        ArrayList<ResOptions> resOptions = new ArrayList<>();
        StringBuilder availableRoom = new StringBuilder();

        try {
            availableRoom.append(availableRoomPart1);
            if(!building.equals("Any")) {
                availableRoom.append("BUILDING = '"+building+"' ");
                availAddAND = true;
            }
            if(!capacity.equals("Any")){
                if(availAddAND) availableRoom.append("AND ");
                availableRoom.append("CAPACITY = "+capacity+" ");
                availAddAND = true;
            }
            if(!av.equals("Any")){
                if(availAddAND) availableRoom.append("AND ");
                if(av.equals("Audiovisual Equipment")){
                    availableRoom.append("AVGEAR = TRUE ");
                }
                else availableRoom.append("AVGEAR = FALSE ");
                availAddAND = true;
            }
            if(!seating.equals("Any")){
                if(availAddAND) availableRoom.append("AND ");
                if(seating.equals("Desks")){
                    availableRoom.append("DESK = TRUE ");
                }
                else availableRoom.append("DESK = FALSE ");
                availAddAND = true;
            }
            if(availAddAND) availableRoom.append(" AND ");
            availableRoom.append(availableRoomPart2);
            availableRoom.append(availRoomDate);
            availableRoom.append(date);
            availableRoom.append(availRoomDate2);
            availableRoom.append(starttime);
            availableRoom.append(availRoomStart);
            availableRoom.append(endtime);
            availableRoom.append(availRoomStart2);
            availableRoom.append(starttime);
            availableRoom.append(availRoomEnd);
            availableRoom.append(endtime);
            availableRoom.append(availRoomEnd2);
            String avlRoomSQL = availableRoom.toString();
            System.out.println(avlRoomSQL);
            availAddAND = false;

            rs = stmt.executeQuery(avlRoomSQL);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            while (rs.next()) {
                ResOptions r = new ResOptions(rs.getInt("CAPACITY"), rs.getBoolean("AVGEAR"),
                        rs.getBoolean("DESK"), 0, rs.getString("BUILDING"),
                        rs.getInt("ROOM"), user, df.parse(date), Time.valueOf(starttime),
                        Time.valueOf(endtime), rs.getInt("ID"));
                resOptions.add(r);
            }
            return FXCollections.observableArrayList(resOptions);
        } catch (SQLException | ParseException e) {
            System.err.println("Exception: " + e.getMessage());
            return FXCollections.observableArrayList(resOptions);
        }
    }

    /**
     * A method used to determine if the username the user submits in the GUI frontend is available
     * within the database. Returns false if a user is found and prompts the user on the GUI that the name
     * is not available.
     * @param username the username the user types in on the register GUI screen
     * @return boolean false is returned if the username is not available, otherwise it returns true
     */
    public boolean isUserNameAvailable(String username){
        try {
            checkUserName.setString(1, username);
            rs = checkUserName.executeQuery();
            if (rs.next()) {
                System.out.println("Found a user!");
                return false;
            } else return true;
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return false;
        }

    }

    /**
     * A method that takes in user input from the GUI front end registration screen and add this data
     * into the database.
     * @param username a String that the user has typed in for their username choice
     * @param fname a String that the user has typed in for their first name
     * @param lname a String that the user has typed in for their last name
     * @param password a String that the user has typed in for their password choice
     * @param userelevel a String that the user has selected what level of user they are, student, faculty/staff
     *                   or admin
     * @return boolean, if the user is succesfully added into the database the function returns true prompting
     * the user on the GUI front end, otherwise it returns false and prompts the user on the GUI.
     */
    public boolean addUser(String username, String fname, String lname, String password, String userelevel) {
        byte[][] pwdvals;
        try {
            userInsert.setString(1, username);
            userInsert.setString(2, fname);
            userInsert.setString(3, lname);
            pwdvals = pwdmgr.generateValues(password);
            userInsert.setBytes(4, pwdvals[1]);
            userInsert.setBytes(5, pwdvals[0]);
            userInsert.setString(6, userelevel);
            userInsert.execute();
            if (userInsert.getUpdateCount() > 0) return true;
            else return false;
        } catch (SQLException e) {
            System.err.println(e.getErrorCode());
            return false;
        }
    }

    /**
     * A method that checks to ensure that the password the user enters matches with the hashed password
     * in the database.
     * @param username a String of the username
     * @param password a String of the password
     * @return boolean, returns true if the password matches the stored hashed password in the database
     * and allows the user to log into the system. Returns false if the password hash doesn't match and prompts
     * the user on the GUI.
     */
    public boolean checkPassword(String username, String password) {
        byte[] pwdhash, salthash;
        try {
            checkPassword.setString(1, username);
            rs = checkPassword.executeQuery();
            if (rs.next()) {
                pwdhash = rs.getBytes("PASSWORD");
                salthash = rs.getBytes("SALT");
                return pwdmgr.validatePassword(password, pwdhash, salthash);
            }
            else return false;
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return false;
        }
    }

    /**
     * A method to make an observable list of all the users eligible to delete from the database.
     * This list is displayed in the GUI as a table view.
     * @param username a String of the username
     * @return UserObList an observable list of the users to delete from the database.
     */
    public ObservableList<User> getUsersToDelete(String username) {
        ArrayList<User> userList = new ArrayList<>();
        try {
            getAllUsers.setString(1, username);
            rs = getAllUsers.executeQuery();
            while (rs.next()) {
                User u = new User(rs.getString("USERNAME"), rs.getString("FIRSTNAME"),
                        rs.getString("LASTNAME"), rs.getString("USERLEVEL"));
                userList.add(u);
            }
            ObservableList<User> UserObList = FXCollections.observableList(userList);
            return UserObList;
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return FXCollections.observableList(new ArrayList<User>());
        }
    }

    /**
     * A method to make an observable list of all the reservations for a particular selected date. This list
     * is displayed in the GUI table view.
     * @param date a String that represents the date the reservations should be displayed for.
     * @return ObsResList an observable list of all the reservations for the specified date
     */
    public ObservableList<Reservation> getAllReservations(String date){
        ArrayList<Reservation> reservations = new ArrayList<Reservation>();
        try{
            getAllReservations.setString(1, date);
            rs = getAllReservations.executeQuery();
            while (rs.next()){
                Reservation r = new Reservation(rs.getInt("ID"),rs.getString("BUILDING"),
                        rs.getInt("ROOM"), rs.getString("USER"), rs.getDate("STARTDATE"),
                        rs.getTime("STARTTIME"), rs.getTime("ENDTIME"));
                reservations.add(r);
            }
            ObservableList<Reservation> ObsResList = FXCollections.observableList(reservations);
            return ObsResList;
        }
        catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return FXCollections.observableList(new ArrayList<Reservation>());
        }
    }
    /**
     * A method to make an observable list of all the reservations for a particular selected date. This list
     * is displayed in the GUI table view. This list is used for registered users so they can delete and
     * like reservations.
     * @param date a String that represents the date the reservations should be displayed for.
     * @return ObsResList an observable list of all the reservations for the specified date
     */
    public ObservableList<LikedReservation>getAllReservationsLiked(String date){
        ArrayList<LikedReservation> reservations = new ArrayList<LikedReservation>();
        try{
            getAllReservationsLiked.setString(1, date);
            rs = getAllReservationsLiked.executeQuery();
            while(rs.next()){
                String temp = (rs.getString("FIRSTNAME")+" " +rs.getString("LASTNAME"));
                LikedReservation r = new LikedReservation(rs.getInt("ID"),rs.getString("BUILDING"),
                        rs.getInt("ROOM"), rs.getString("USER"), rs.getDate("STARTDATE"),
                        rs.getTime("STARTTIME"), rs.getTime("ENDTIME"), temp,
                        rs.getInt("LIKECOUNT"));
                reservations.add(r);
            }
            ObservableList<LikedReservation> ObsResList = FXCollections.observableList(reservations);
            return ObsResList;
        }
        catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return FXCollections.observableList(new ArrayList<LikedReservation>());
        }
    }

    /**
     * A method to delete specific users as an administrator of the system.
     * @param users an observable list of users that the administrator wants to delete from the system
     * @return int usercount this int  ensures that the number of users to delete is updated if the query
     * executes correctly.
     */
    public int deleteUsers(ObservableList<User> users){
        try {
            int usercount = 0;
            for (User u: users) {
                deleteUsers.setString(1, u.getName());
                usercount += deleteUsers.executeUpdate();
            }
            return usercount;
        }
        catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return 0;
        }
    }

    /**
     * A method to delete reservations from the h2 database
     * @param res An observable list of reservations to delete
     * @return the count of the reservations deleted
     */
    public int deleteReservation(ObservableList<LikedReservation> res){
        try {
            int count = 0;
            for (LikedReservation r: res) {
                deleteReservation.setInt(1, r.getId());
                count += deleteReservation.executeUpdate();
            }
            System.out.println(deleteReservation.toString());
            return count;
        }
        catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return 0;
        }
    }

    /**
     * A method to return the specific user's data fields.
     * @param username a String of the username
     * @return A string array of the user's data fields, their username, first/last name and their user level.
     */
    public String[] getUser(String username){
        String[] user = new String[4];
        try {
            getUser.setString(1, username);
            rs = getUser.executeQuery();
            if (rs.next()) {
                user[0] = rs.getString("USERNAME");
                user[1] = rs.getString("FIRSTNAME");
                user[2] = rs.getString("LASTNAME");
                user[3] = rs.getString("USERLEVEL");
                return user;
            }
            else{
                user[0] = "NULL";
                return user;
            }
        }
        catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            user[0] = "NULL";
            return user;
        }
    }

    /**
     * A method which updates the likes count for a reservation in the database. The method uses an observable
     * list of reservations that the user selects from in the GUI display table.
     * @param username String of the username associated with making the likes
     * @param list an observable list of reservations that the user is liking
     * @return likecount which is an int that is used to display to the user how many reservations that
     * they liked.
     */
    public int addLikes(String username, ObservableList<LikedReservation> list) {
        try {
            int likecount = 0;
            for (LikedReservation l : list) {
                addLikes.setInt(1, l.getId());
                addLikes.setString(2, username);
                System.out.println(addLikes.toString());
                likecount += addLikes.executeUpdate();
            }
            return likecount;
        }
         catch (SQLException e) {
            System.err.println(e.getErrorCode());
            return 0;
        }
    }

    /**
     * A method that creates the reservations list that a user can select from to delete from on the management
     * GUI screen.
     * @param date the date as a String for which the user has selected to delete reservations from
     * @param user A string representing the user of the system
     * @return the observable list of reservations the user can delete from.
     */
    public ObservableList<LikedReservation> deletableReservations(String date, User user){
        ArrayList<LikedReservation> reservations = new ArrayList<LikedReservation>();
        try{
            if(user.getPriv().equals("Faculty")) {
                showDeletable.setString(1, date);
                showDeletable.setString(2, user.getName());
                rs = showDeletable.executeQuery();
            }
            else if(user.getPriv().equals("Student")){
                deleteStudentRes.setString(1, date);
                deleteStudentRes.setString(2, user.getName());
                rs = deleteStudentRes.executeQuery();
            }
            while(rs.next()){
                String temp = (rs.getString("FIRSTNAME")+" " +rs.getString("LASTNAME"));
                LikedReservation r = new LikedReservation(rs.getInt("ID"),rs.getString("BUILDING"),
                        rs.getInt("ROOM"), rs.getString("USER"), rs.getDate("STARTDATE"),
                        rs.getTime("STARTTIME"), rs.getTime("ENDTIME"), temp,
                        rs.getInt("LIKECOUNT"));
                reservations.add(r);
            }
            ObservableList<LikedReservation> ObsResList = FXCollections.observableList(reservations);
            return ObsResList;
        }
        catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return FXCollections.observableList(new ArrayList<LikedReservation>());
        }
    }

    public int addReservation(ObservableList<ResOptions> reservation){
        try {
            addReservation.setString(1, reservation.get(0).getDate());
            addReservation.setString(2,fixTime(reservation.get(0).getStartTime()));
            addReservation.setString(3,fixTime(reservation.get(0).getEndTime()));
            addReservation.setString(4,reservation.get(0).getUser());
            addReservation.setInt(5,reservation.get(0).getLocId());
            return addReservation.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return 0;
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
}

