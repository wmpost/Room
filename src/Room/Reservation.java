
package Room;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {

    private int id;
    private String building;
    private int room;
    private String user;
    private String date;
    private String startTime;
    private String endTime;
    private int likes;

    public Reservation(int i,  String b, int r, String u, Date d, Time s, Time e){
        id = i;
        building = b;
        room = r;
        user = u;
        date = dateFormatter(d);
        startTime = timeFormatter(s);
        endTime = timeFormatter(e);
    }

    private String dateFormatter(Date d){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    private String timeFormatter(Time t){
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
