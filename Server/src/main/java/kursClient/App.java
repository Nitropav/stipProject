package kursClient;


import kursClient.view.ServerWindow;

import java.net.UnknownHostException;

public class App {
    public static void main(String[] args) throws UnknownHostException {
        ServerWindow mainPage = new ServerWindow();
        mainPage.setLocationRelativeTo(null);
        mainPage.setVisible(true);
    }
}

