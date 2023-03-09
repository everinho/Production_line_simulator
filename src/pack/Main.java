package pack;

public class Main
{
    public static void main(String[] args)
    {
        Users uzytkownicy = new Users();
        LoginPage logowanie = new LoginPage(uzytkownicy.get_logins());
    }
}
