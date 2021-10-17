package Project2;

import java.awt.BorderLayout;

import javax.swing.*;

public class GUI1024 { 
    public static void main(String arg[]){
        JFrame gui = new JFrame ("Welcome to 1024!"); 
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        GUI1024Panel panel = new GUI1024Panel();
        //panel.setFocusable(true);
        gui.getContentPane().add(panel);
        //gui.add(panel.p);
        gui.setJMenuBar(panel.mb);
        gui.setSize(panel.getSize());
        gui.setVisible(true);
    }  
}
