package pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class LoginPage implements ActionListener
{
    JFrame frame = new JFrame();
    JButton zaloguj = new JButton("Zaloguj");
    JButton reset = new JButton("Wyczyść");
    JTextField wpisz_login = new JTextField();
    JPasswordField wpisz_haslo = new JPasswordField();
    JLabel login_label = new JLabel("Login:");
    JLabel haslo_label = new JLabel("Hasło:");
    JLabel komunikat = new JLabel();
    HashMap<String,String> logins = new HashMap<String,String>();
    LoginPage(HashMap<String,String> loginsOriginal)
    {
        logins=loginsOriginal;

        login_label.setBounds(50,100,75,25);
        haslo_label.setBounds(50,150,75,25);
        komunikat.setBounds(85,250,250,35);
        komunikat.setFont(new Font(null, Font.BOLD,21));

        wpisz_login.setBounds(125,100,200,25);
        wpisz_haslo.setBounds(125,150,200,25);

        zaloguj.setBounds(125,200,100,25);
        zaloguj.setFocusable(false);
        zaloguj.addActionListener(this);

        reset.setBounds(225,200,100,25);
        reset.setFocusable(false);
        reset.addActionListener(this);

        frame.add(login_label);
        frame.add(haslo_label);
        frame.add(komunikat);
        frame.add(wpisz_login);
        frame.add(wpisz_haslo);
        frame.add(zaloguj);
        frame.add(reset);


        frame.setTitle("Panel logowania");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==reset)
        {
            wpisz_login.setText("");
            wpisz_haslo.setText("");
        }
        if(e.getSource()==zaloguj)
        {
            String login = wpisz_login.getText();
            String haslo = String.valueOf(wpisz_haslo.getPassword());

            if(logins.containsKey(login))
            {
                if(logins.get(login).equals(haslo))
                {
                    komunikat.setForeground(Color.GREEN);
                    komunikat.setText("Pomyślnie zalogowano!");
                    frame.dispose();
                    Okno okno = new Okno(login);
                }
                else
                {
                    komunikat.setForeground(Color.RED);
                    komunikat.setText("Niepoprawne hasło!");
                }
            }
            else
            {
                komunikat.setForeground(Color.red);
                komunikat.setText("Niepoprawny login!");
            }
        }
    }
}
