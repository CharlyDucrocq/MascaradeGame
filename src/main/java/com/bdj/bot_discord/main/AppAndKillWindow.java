package com.bdj.bot_discord.main;
import javax.security.auth.login.LoginException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AppAndKillWindow extends JFrame{
    public static void main(String[] args) throws LoginException {
        Application.main(args);
        launchWindow();
    }

    private static void launchWindow(){
        JFrame f=new JFrame("ShutDown window");
        JButton b=new JButton("ShutDown the run");
        b.setBounds(10,10,220,30);
        f.add(b);
        b.addActionListener(AppAndKillWindow::buttonAction);
        f.setSize(250,90);
        f.setLayout(null);
        f.setVisible(true);
    }

    private static void buttonAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
