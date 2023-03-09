package pack;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

public class Okno extends JFrame implements ActionListener {
    Users uzytkownicy = new Users();
    JLabel user = new JLabel("");
    //Obecnosc
    JButton obecny = new JButton("Obecny");
    JLabel obecnosc_time = new JLabel("");
    JLabel obecnosc_label = new JLabel("Czas nieaktywności:");
    JLabel brak_aktywnosci = new JLabel("");
    //Procesor
    JLabel procesor_label = new JLabel("Wykorzystanie procesora [%]");
    JSlider procesor_zmien = new JSlider(0,100,70);
    JLabel procesor_temp = new JLabel("");
    //Wentylatory
    JLabel wents = new JLabel("Wentylatory");
    Wentylator went1 = new Wentylator(100,100);
    Wentylator went2 = new Wentylator(100,100);
    JSlider went_zmien = new JSlider(0,50,30);
    JLabel went_predkosc = new JLabel("");
    //Komunikaty
    JLabel komunikat = new JLabel("AUTODIAGNOSTYKA WYŁĄCZONA");
    //Główny panel
    JLabel autodiagnostyka_label = new JLabel("Autodiagnostyka produkcji WYŁĄCZONA");
    JButton autodiagnostyka = new JButton("Włącz");
    JLabel przytomnosc_label = new JLabel("Autodiagnostyka operatora WYŁĄCZONA");
    JButton przytomnosc = new JButton("Włącz");
    JLabel linia_predkosc = new JLabel("");
    JSlider linia_steruj = new JSlider(0,100,60);
    JLabel linia = new JLabel();
    JLabel silnik_temperature = new JLabel();
    JLabel silnik_napis = new JLabel("Temperatura silnika: ");
    JLabel tlo = new JLabel();

    Timer t;
    int i = 0;
    Timer t_awarii;
    int j=0;
    int rodzaj_awarii;
    int last_awaria=0;
    double silnik_temp=50;
    double temp=60;
    int predkosc_wentylatorow = 1600;
    int predkosc_linii = 60;
    int wykorzystanie_cpu = 70 ;
    double wsp = 0.85;
    boolean wlaczone = false;
    boolean diag_wlaczone = false;
    boolean if_przytomnosc = false;
    Okno(String login)
    {
        tlo.setBounds(0,0,1280,720);
        tlo.setBackground(Color.lightGray);

        ImageIcon img = new ImageIcon("src\\adds\\linia_foto.jpg");
        linia.setBounds(620,50,640,360);
        linia.setIcon(img);

        ImageIcon img2 = new ImageIcon("src\\adds\\temperature.jpg");
        silnik_temperature.setBounds(300,100,183,315);
        silnik_temperature.setIcon(img2);

        silnik_napis.setBounds(300,50,300,50);
        silnik_napis.setFont(new Font(null,Font.BOLD,20));
        silnik_temp=30+(predkosc_linii*80)/100;
        silnik_napis.setText("Temperatura silnika: "+silnik_temp+" °C");

        t = new Timer(1000, this);

        t_awarii = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                j++;
                if(j==10)
                {
                    Random  r = new Random();
                    rodzaj_awarii = r.nextInt(3)+1;
                    while(rodzaj_awarii==last_awaria)
                    {
                        rodzaj_awarii = r.nextInt(3)+1;
                    }
                    last_awaria=rodzaj_awarii;
                    switch(rodzaj_awarii)
                    {
                        case 1: komunikat.setText("AWARIA - ZWOLNIJ PRĘDKOŚĆ LINII!");
                            komunikat.setForeground(Color.RED);
                            break;
                        case 2: komunikat.setText("AWARIA - ZMNIEJSZ PRĘDKOŚĆ WENTYLATORÓW!");
                            komunikat.setForeground(Color.RED);
                            break;
                        case 3: komunikat.setText("AWARIA - ZMNIEJSZ WYKORZYSTANIE CPU!");
                            komunikat.setForeground(Color.RED);
                            break;
                    }
                }
                //naprawianie awarii
                if(rodzaj_awarii==1)
                {
                    if(predkosc_linii<40)
                    {
                        t_awarii.stop();
                        komunikat.setText("AWARIA POPRAWNIE NAPRAWIONA");
                        komunikat.setForeground(Color.GREEN);
                        j=0;
                        t_awarii.start();
                        rodzaj_awarii=0;
                    }
                }
                if(rodzaj_awarii==2)
                {
                    if(predkosc_wentylatorow<1100)
                    {
                        t_awarii.stop();
                        komunikat.setText("AWARIA POPRAWNIE NAPRAWIONA");
                        komunikat.setForeground(Color.GREEN);
                        j=0;
                        t_awarii.start();
                        rodzaj_awarii=0;
                    }
                }
                if(rodzaj_awarii==3)
                {
                    if(wykorzystanie_cpu<40)
                    {
                        t_awarii.stop();
                        komunikat.setText("AWARIA POPRAWNIE NAPRAWIONA");
                        komunikat.setForeground(Color.GREEN);
                        j=0;
                        t_awarii.start();
                        rodzaj_awarii=0;
                    }
                }
                if(j==25)
                {
                    komunikat.setText("POWAŻNA AWARIA - ZAMYKANIE PROCESU!");
                    komunikat.setForeground(Color.RED);
                    alarm_sound();
                }
                if(j==32)
                {
                    LoginPage lp = new LoginPage(uzytkownicy.get_logins());
                    dispose();
                }
            }
        });

        went1.setBackground(Color.gray);
        went1.setBounds(30,100,100,100);
        went2.setBackground(Color.gray);
        went2.setBounds(130,100,100,100);
        went_zmien.setBounds(30,250,200,50);
        went_zmien.setPaintTicks(true);
        went_zmien.setMinorTickSpacing(5);
        went_zmien.setPaintTrack(true);
        went_zmien.setMajorTickSpacing(10);

        went_predkosc.setBounds(30,200,270,50);
        went_predkosc.setFont(new Font(null,Font.BOLD, 15));

        wents.setFont(new Font(null,Font.BOLD,20));
        wents.setBounds(80,50,300,50);

        went_zmien.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int a;
                a=50-went_zmien.getValue();
                went1.time=a;
                wsp=120-went_zmien.getValue();
                wsp=wsp/100;
                predkosc_wentylatorow = went_zmien.getValue()*50+100;
                went_predkosc.setText("Prędkość wentylatorów: "+predkosc_wentylatorow+" obr/min");
            }
        });

        obecnosc_time.setBounds(1140,550,100,40);
        obecnosc_time.setFont(new Font(null,Font.BOLD,17));

        obecnosc_label.setBounds(1050,500,200,50);
        obecnosc_label.setFont(new Font(null,Font.BOLD,17));

        brak_aktywnosci.setBounds(1000,450,250,50);
        brak_aktywnosci.setFont(new Font(null,Font.BOLD,15));

        obecny.setBounds(1080,600,100,40);
        obecny.setFocusable(false);
        obecny.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(if_przytomnosc==true)
                {
                    brak_aktywnosci.setText("");
                    t.stop();
                    i=0;
                    t.start();
                }
            }
        });

        procesor_label.setBounds(70,530,240,50);
        procesor_label.setFont(new Font(null,Font.BOLD,17));

        procesor_zmien.setBounds(70,580,240,50);
        procesor_zmien.setPaintTicks(true);
        procesor_zmien.setMinorTickSpacing(10);
        procesor_zmien.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                wykorzystanie_cpu=procesor_zmien.getValue();
                temp=Math.round(procesor_zmien.getValue()*wsp*100.0)/100.0;
                procesor_temp.setText("Temperatura rdzenia wynosi: "+temp+" °C");
                if(diag_wlaczone==true)
                {
                    if(temp>65)
                    {
                        procesor_temp.setForeground(Color.RED);
                        komunikat.setText("ZBYT WYSOKA TEMPERATURA RDZENIA!");
                        komunikat.setForeground(Color.RED);
                    }
                    if(temp<=65)
                    {
                        procesor_temp.setForeground(Color.GREEN);
                        komunikat.setText("BRAK NIEZBĘDNYCH DZIAŁAŃ");
                        komunikat.setForeground(Color.DARK_GRAY);
                    }
                }
                else {
                    komunikat.setText("AUTODIAGNOSTYKA WYŁĄCZONA");
                    komunikat.setForeground(Color.DARK_GRAY);
                }
            }
        });
        procesor_zmien.setPaintTrack(true);
        procesor_zmien.setMajorTickSpacing(20);

        procesor_zmien.setPaintLabels(true);
        procesor_zmien.setFont(new Font(null,Font.BOLD, 15));
        procesor_temp.setBounds(60,500,270,50);
        procesor_temp.setFont(new Font(null,Font.BOLD, 15));

        user.setBounds(5,0,200,30);
        user.setText("Zalogowano jako "+ login);
        user.setForeground(Color.cyan);
        user.setFont(new Font(null,Font.BOLD,13));

        komunikat.setBounds(300,0,680,50);
        komunikat.setVerticalAlignment(SwingConstants.CENTER);
        komunikat.setHorizontalTextPosition(SwingConstants.CENTER);
        komunikat.setFont(new Font(null,Font.BOLD,25));
        komunikat.setForeground(Color.DARK_GRAY);

        autodiagnostyka_label.setBounds(500,500,300,40);
        autodiagnostyka_label.setFont(new Font(null,Font.BOLD,14));
        autodiagnostyka_label.setForeground(Color.RED);
        autodiagnostyka.setBounds(800,500,100,40);
        autodiagnostyka.setFocusable(false);
        autodiagnostyka.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(diag_wlaczone==false)
                {
                    autodiagnostyka_label.setText("Autodiagnostyka produkcji WŁĄCZONA");
                    autodiagnostyka_label.setForeground(Color.GREEN);
                    autodiagnostyka.setText("Wyłącz");
                    diag_wlaczone=true;
                    t_awarii.start();
                }
                else
                {
                    t_awarii.stop();
                    autodiagnostyka_label.setText("Autodiagnostyka produkcji WYŁĄCZONA");
                    autodiagnostyka.setText("Włącz");
                    autodiagnostyka_label.setForeground(Color.RED);
                    diag_wlaczone=false;
                    procesor_temp.setForeground(Color.darkGray);
                    j=0;
                }
            }
        });

        przytomnosc_label.setBounds(500,570,300,40);
        przytomnosc_label.setFont(new Font(null,Font.BOLD,14));
        przytomnosc_label.setForeground(Color.RED);
        przytomnosc.setBounds(800,570,100,40);
        przytomnosc.setFocusable(false);
        przytomnosc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(if_przytomnosc==false)
                {
                    przytomnosc_label.setText("Autodiagnostyka operatora WŁĄCZONA");
                    przytomnosc_label.setForeground(Color.GREEN);
                    przytomnosc.setText("Wyłącz");
                    if_przytomnosc=true;
                    i=0;
                    t.start();
                }
                else
                {
                    przytomnosc_label.setText("Autodiagnostyka operatora WYŁĄCZONA");
                    przytomnosc.setText("Włącz");
                    przytomnosc_label.setForeground(Color.RED);
                    if_przytomnosc=false;
                    t.stop();
                }
            }
        });

        linia_predkosc.setBounds(30,300,270,50);
        linia_predkosc.setFont(new Font(null,Font.BOLD, 15));

        linia_steruj.setBounds(30,350,200,50);
        linia_steruj.setPaintTicks(true);
        linia_steruj.setPaintTrack(true);
        linia_steruj.setMajorTickSpacing(10);
        linia_steruj.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                predkosc_linii=linia_steruj.getValue();
                linia_predkosc.setText("Prędkość linii wynosi: "+predkosc_linii+" %");
                silnik_temp=30+(predkosc_linii*80)/100;
                silnik_napis.setText("Temperatura silnika: "+silnik_temp+" °C");
            }
        });

        //Ustawienia okna
        setTitle("Symulator linii produkcyjnej");
        setResizable(false);
        setSize(1280,720);
        setVisible(true);
        setLayout(null);
        setBackground(Color.lightGray);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Dodanie wszystkich elementów
        add(obecny);
        add(obecnosc_time);
        add(obecnosc_label);
        add(brak_aktywnosci);
        add(procesor_zmien);
        add(procesor_label);
        add(procesor_temp);
        add(went1);
        add(went2);
        add(went_zmien);
        add(komunikat);
        add(linia_predkosc);
        add(linia_steruj);
        add(user);
        add(went_predkosc);
        add(autodiagnostyka);
        add(autodiagnostyka_label);
        add(przytomnosc_label);
        add(przytomnosc);
        add(wents);
        add(silnik_napis);
        add(linia);
        add(silnik_temperature);
        add(tlo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        i++;
        obecnosc_time.setText(""+(i));
        if(i==20)
        {
            brak_aktywnosci.setForeground(Color.RED);
            brak_aktywnosci.setText("Potwierdź obecność w ciągu 10s!");
        }
        if(i==30)
        {
            LoginPage lp = new LoginPage(uzytkownicy.get_logins());
            dispose();
        }
    }
    public void alarm_sound()
    {
        try{
            File alarm = new File("src\\adds\\alarm.wav");
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(alarm));
            c.start();
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

}
