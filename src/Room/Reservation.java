
package Room;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Whitney
 * The Reservation.java class is used to locally keep track of reservations and their data members while the
 * system is running. The class contains data fields that match with the same fields in the h2 database.
 * Their id number, the building name, the room number, the user associated with the reservation, the date
 * of the reservation and the start/end times.
 */

public class Reservation {

    protected int id;
    protected String building;
    protected int room;
    protected String user;
    protected String date;
    protected String startTime;
    protected String endTime;

        public Reservation(){
        id = 0;
        building = null;
        room = 0;
        user = null;
        date = null;
        startTime = null;
        endTime = null;
    }

    /**
     * Non default constructor for creating a reservation
     * @param i int, the id number
     * @param b String the building number
     * @param r int, the room number
     * @param u String, the username
     * @param d d, the date of the reservation
     * @param s s, the start time
     * @param e e, the end time
     */
    public Reservation(int i,  String b, int r, String u, Date d, Time s, Time e){
        id = i;
        building = b;
        room = r;
        user = u;
        date = dateFormatter(d);
        startTime = timeFormatter(s);
        endTime = timeFormatter(e);
    }

    /**
     * A method to format the date
     * @param d the date
     * @return the String of the formatted date
     */
    protected String dateFormatter(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    /**
     * A method to format the time
     * @param t the time
     * @return the String of the formatted time
     */
    protected String timeFormatter(Time t){
        SimpleDateFormat stf = new SimpleDateFormat("hh:mm");
        return stf.format(t);
    }

    /**
     * A method to return the building
     * @return the building
     */
    public String getBuilding() {
        return building;
    }

    /**
     * A method to get the start time
     * @return the start time
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * A method to get the date
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * A method to get the id
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * A method to get the room number
     * @return the room number
     */
    public int getRoom() {
        return room;
    }

    /**
     * A method to get the end time
     * @return the end time
     */
    public String getEndTime() {
        return endTime;
    }

    public String getUser(){
        return user;
    }
}
