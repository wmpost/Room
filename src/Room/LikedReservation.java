package Room;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LikedReservation extends Reservation{

    private String fullName;
    private int likes;

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

    public String getFullName() {
        return fullName;
    }

    public int getLikes() {
        return likes;
    }
}


