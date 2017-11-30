package DB;

import java.sql.*;

import Room.LikedReservation;
import Room.Reservation;
import Room.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.h2.Driver;
import java.lang.StringBuilder;
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

    private PreparedStatement testSt = null;
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



        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public String test(){
        try {
            rs = testSt.executeQuery();
            if (rs.next()) {
                return rs.getString("PWD");
            }
            else return "WTF";
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return "WTF";
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
            System.out.println(deleteUsers.toString());
            return usercount;
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
}

