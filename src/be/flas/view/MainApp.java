package be.flas.view;

import java.awt.EventQueue;

public class MainApp {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    
                    FormInstructor frame = new FormInstructor();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
