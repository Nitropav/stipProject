package kursClient;


import kursClient.view.AuthorizationFrame.AuthorizationWindow;

public class App {
    public static void main(String[] args) {
        AuthorizationWindow authorizationWindow = new AuthorizationWindow();
        authorizationWindow.setVisible(true);
        authorizationWindow.setLocationRelativeTo(null);
    }
}
