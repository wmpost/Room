package Room;

import java.sql.Time;
import java.util.Date;

/**
 * @author Whitney
 * The ResOptions class extends the Reservation class and is used when populating the search results when
 * a system user is searching for a room to reserve. It adds a few data fields that aren't needed when
 * displaying an already reserved room.
 */
public class ResOptions extends Reservation {

    private int capacity;
    private boolean av;
    private boolean seating;

    public ResOptions(int c, boolean a, boolean se, int i, String b, int r, String u, Date d, Time s, Time e){
        capacity = c;
        av = a;
        seating = se;
        id = i;
        building = b;
        room = r;
        user = u;
        date = dateFormatter(d);
        startTime = timeFormatter(s);
        endTime = timeFormatter(e);
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean getAv() {
        return av;
    }

    public boolean getSeating() {
        return seating;
    }
}
