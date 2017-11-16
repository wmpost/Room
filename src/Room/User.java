package Room;

public class User {

    private String name;
    private String fname;
    private String lname;
    private String priv;

    public User(String u, String f, String l, String p){
        name = u;
        fname = f;
        lname = l;
        priv = p;
    }

    public String getLname() {
        return lname;
    }

    public String getFname() {
        return fname;
    }

    public String getName() {
        return name;
    }

    public void setPriv(String priv) {
        this.priv = priv;
    }

    public String getPriv() {
        return priv;
    }

    public void setfName(String fname) {
        this.fname = fname;
    }
    public void setlName(String lname) {
        this.lname = lname;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void userClear(){
        this.setfName("null");
        this.setlName("null");
        this.setName("null");
        this.setPriv("null");
    }
}
