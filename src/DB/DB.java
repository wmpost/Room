package DB;
import java.sql.*;

import Room.Reservation;
import Room.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.h2.Driver;
import java.lang.StringBuilder;
import java.util.ArrayList;

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

    private PreparedStatement testSt = null;
    private PwdMgr pwdmgr;

    public DB() {
        userInsertString = "INSERT INTO USER(username, firstname, lastname, password, salt, userlevel) VALUES(?, ?, ?, ?, ?, ?);";
        checkPasswordString = "SELECT PASSWORD, SALT FROM USER WHERE USERNAME=?";
        checkUserNameString = "SELECT USERNAME FROM USER WHERE USERNAME=?";
        getUserString = "SELECT USERNAME, FIRSTNAME, LASTNAME, USERLEVEL FROM USER WHERE USERNAME=?";
        getAllReservationsString = "SELECT RESERVATION.*, LOCATION.BUILDING, LOCATION.ROOM FROM RESERVATION JOIN LOCATION ON RESERVATION.LOCATION = LOCATION.ID WHERE RESERVATION.STARTDATE=? ORDER BY STARTDATE, STARTTIME, LOCATION ";
        getAllUsersString ="SELECT * FROM USER WHERE USERNAME != ?";
        deleteUserString = "DELETE FROM USER WHERE USERNAME IN (?)";

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

    public int deleteUsers(ObservableList<User> users){
        StringBuilder build = new StringBuilder();
        for (int i = 0; i <users.size() ; i++) {
            build.append(users.get(i).getName());
            if(i != users.size()-1) build.append(", ");
        }
        try {
            deleteUsers.setString(1, build.toString());
            System.out.println(deleteUsers.toString());
            return deleteUsers.executeUpdate();
        }
        catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
            return 0;
        }
    }

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
}

