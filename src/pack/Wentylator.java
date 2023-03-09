package pack;

import javax.swing.*;
import java.awt.*;

public class Wentylator extends JPanel
{
    int x;
    int y;
    int fanWidth=100;
    int fanHeight=100;
    int centerX;
    int centerY;
    int angle=0;
    int time=15;
    Wentylator(int w,int h)
    {
        this.centerX=w/2;
        this.centerY=h/2;
        this.x=centerX-fanWidth/2;
        this.y=centerY-fanHeight/2;
    }
    public void paint(Graphics g)
    {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillArc(x,y,fanWidth,fanHeight,angle,30);
        g.fillArc(x,y,fanWidth,fanHeight,angle+120,30);
        g.fillArc(x,y,fanWidth,fanHeight,angle+240,30);
        angle=(angle +30)%360;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        repaint();
    }
}
