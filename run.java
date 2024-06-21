package test;

import javax.swing.SwingUtilities;
import View.ServerUI;

public class run {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ServerUI serverUI = new ServerUI();
                serverUI.setVisible(true);
            }
        });
    }
}
