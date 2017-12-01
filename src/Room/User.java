package Room;

/**
 * @author Whitney
 * A class that locally creates objects to store the information on the current user of the system or of multiple users when
 * displaying data from the database. The data fields for this class matches some from the h2 database, the fields include the
 * user's first and last name, their username and their privilege level.
 */
public class User {

    private String name;
    private String fname;
    private String lname;
    private String priv;

    /**
     * Non default constructor for user.
     * @param u the username
     * @param f the first name
     * @param l the last name
     * @param p the privilege level
     */
    public User(String u, String f, String l, String p){
        name = u;
        fname = f;
        lname = l;
        priv = p;
    }

    /**
     * A method to return the last name
     * @return the last name
     */
    public String getLname() {
        return lname;
    }

    /**
     * A method to return the first name
     * @return the first name
     */
    public String getFname() {
        return fname;
    }

    /**
     * A method to get the username
     * @return the username
     */
    public String getName() {
        return name;
    }

    /**
     * A method to set the privilege level of the user
     * @param priv the privilege level
     */
    public void setPriv(String priv) {
        this.priv = priv;
    }

    /**
     * A method to get the privilege level of the user
     * @return priv the privilege level
     */
    public String getPriv() {
        return priv;
    }

    /**
     * A method to set the user's first name
     * @param fname String for first name
     */
    public void setfName(String fname) {
        this.fname = fname;
    }

    /**
     * A method that sets the last name
     * @param lname String for last name
     */
    public void setlName(String lname) {
        this.lname = lname;
    }

    /**
     * A method to set the username
     * @param name String for username
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * A method to clear the user fields
     */
    public void userClear(){
        this.setfName("null");
        this.setlName("null");
        this.setName("null");
        this.setPriv("null");
    }
}
