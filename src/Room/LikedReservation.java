package Room;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Whitney
 * A class that extends the Reservation class in order to add the full name and likes to the reservation class,
 * this data is needed when displaying the reservation calendar to registered users instead of guest users.
 * It contains two new data field, a String called fullName and an int for the number of likes.
 */

public class LikedReservation extends Reservation{

    private String fullName;
    private int likes;

    /**
     * The non default constructor for LikedReservation
     * @param i an int representing the id number from the database table
     * @param b a String representing the building name
     * @param r a String representing the room
     * @param u a String representing the username
     * @param d a Date for the date of the reservation
     * @param s a starting time for the reservation
     * @param e the ending time of the reservation
     * @param fn A String for the fullname of the user with the reservation
     * @param l an int for the like count on the reservation
     */
    public LikedReservation(int i, String b, int r, String u, Date d, Time s, Time e, String fn, int l){
        this.id = i;
        this.building = b;
        this.room = r;
        this.user = u;
        this.date = dateFormatter(d);
        this.startTime = timeFormatter(s);
        this.endTime = timeFormatter(e);
        fullName = fn;
        likes = l;
    }

    /**
     * A method to return the full name of the user
     * @return String of the fullname
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * A method that returns the likes
     * @return int the number of likes
     */
    public int getLikes() {
        return likes;
    }
}


