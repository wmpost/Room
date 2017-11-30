
package Room;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    public Reservation(int i,  String b, int r, String u, Date d, Time s, Time e){
        id = i;
        building = b;
        room = r;
        user = u;
        date = dateFormatter(d);
        startTime = timeFormatter(s);
        endTime = timeFormatter(e);
    }

    protected String dateFormatter(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    protected String timeFormatter(Time t){
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
        return stf.format(t);
    }


    public String getBuilding() {
        return building;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public int getRoom() {
        return room;
    }

    public String getEndTime() {
        return endTime;
    }


}
