package com.github.drizzleme.client.ui;

import org.jb2011.lnf.beautyeye.utils.WindowTranslucencyHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with drizzleIM
 *
 * @author ; DRIZZLEME
 *         DATE : 2016/9/22
 */
public class Toast extends JDialog implements ActionListener{
    private Point showPossition = null;
    private Timer timer = null;
    private ToastPane toastPane = null;

    public Toast(int delay, String message, Point p)
    {
//		super(parent);
        initGUI();

        // init datas
        timer = new Timer(delay, this);
        toastPane.setMessage(message);
        this.showPossition = p;
    }

    protected void initGUI()
    {
        // set dialog full transparent
        this.setAlwaysOnTop(true);
        this.setUndecorated(true);
//		AWTUtilities.setWindowOpaque(this, false);
        WindowTranslucencyHelper.setWindowOpaque(this, false);
//		this.setBackground(new Color(0,0,0,0));
        // contentPane default is opaque in Java1.7+
        ((JComponent)(this.getContentPane())).setOpaque(false);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        // init main layout
        toastPane = new ToastPane();
        this.add(toastPane);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // fade out
        for(float i=1.0f; i>=0;i-=0.05f)
            try{
//			AWTUtilities.setWindowOpacity(this, i);
                WindowTranslucencyHelper.setOpacity(this, i);
                Thread.sleep(50);
            }
            catch (Exception e2){
            }

        // dispose it
        if(timer != null)
            timer.stop();
        this.dispose();
    }

    public Toast showItNow()
    {
        this.pack();
        if(showPossition == null || (showPossition.x<0&&showPossition.y<0))
            this.setLocationRelativeTo(null);
        else
            this.setLocation(new Point(showPossition.x<0?0:showPossition.x, showPossition.y<0?0:showPossition.y));
        this.setVisible(true);
        timer.start();
        return this;
    }

    public static Toast showTost(int delay, String message, Point p)
    {
        return new Toast(delay, message, p).showItNow();
    }
}
