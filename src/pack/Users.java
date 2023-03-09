package pack;

import java.util.HashMap;

public class Users
{
    HashMap<String,String> logins = new HashMap<String,String>();

    Users()
    {
        logins.put("admin","admin");
        logins.put("test","test");
        logins.put("student","projekt");
    }

    protected HashMap get_logins()
    {
        return logins;
    }
}
