package DB;

import org.junit.Test;

public class DBTest {

    @Test
    public void available() throws Exception{
        DB db = new DB();
        System.out.println(db.isUserNameAvailable("WPOST"));
    }
    @Test
    public void addUser() throws Exception {
       DB base = new DB();
       System.out.println(base.addUser("y", "y", "y", "y", "Student"));
    }

    @Test
    public void checkPassword() throws Exception {
        DB base = new DB();
        System.out.println(base.checkPassword("y", "y"));
    }


}