package kursClient.view.AuthorizationFrame;
import kursClient.entities.Admin;
import kursClient.view.menu.AdminWindow;
import kursClient.view.menu.UserWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

public class AuthorizationWindow extends JFrame {

    private JLabel authLabel;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JTextField login;
    private JPasswordField password;
    private JButton ok;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    public AuthorizationWindow() {
        super("Авторизация");
        setSize(300, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        init();

        ok.addActionListener(event -> {
            try {
                if (login.getText().isEmpty() || String.copyValueOf(password.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Есть незаполненные поля");
                    return;
                }
                createSocket();
                if (socket == null) {
                    return;
                }
                objectOutputStream.writeObject("authorisation");
                Admin admin = new Admin();
                admin.setLogin(login.getText());
                admin.setPassword(String.copyValueOf(password.getPassword()));

                objectOutputStream.writeObject(admin);

                String answer = (String) objectInputStream.readObject();

                login.setText("");
                password.setText("");
                if (!answer.equals("false")) {
                    if (answer.equals("Admin")) {
                        AdminWindow adminWindow = new AdminWindow(this, socket, objectOutputStream, objectInputStream);
                        adminWindow.setVisible(true);
                        adminWindow.setLocationRelativeTo(null);
                    } else {
                        UserWindow userWindow = new UserWindow(this, socket, objectOutputStream, objectInputStream, answer);
                        userWindow.setVisible(true);
                        userWindow.setLocationRelativeTo(null);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Неправильно введен логин или пароль");
                }

            } catch (SocketException e) {
                JOptionPane.showMessageDialog(this,
                        "Вы не подключены к серверу");
                socket = null;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void createSocket() {
        try { // установка соединения с сервером
            if (socket == null || objectOutputStream == null || objectInputStream == null) {
//                socket = new Socket(InetAddress.getLocalHost(), 8071);
                socket = new Socket(InetAddress.getByName("127.0.0.1"), 8080);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
            }
        } catch (ConnectException e) {
            System.out.println("e " + e);
            JOptionPane.showMessageDialog(this, "Вы не подключены к серверу");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        authLabel = new JLabel("Авторизация");
        authLabel.setFont(new Font("Serif", Font.BOLD, 20));
        authLabel.setLocation(80, 10);
        authLabel.setSize(140, 50);


        loginLabel = new JLabel("Логин: ");
        loginLabel.setLocation(113, 50);
        loginLabel.setSize(75, 50);

        login = new JTextField();
        login.setLocation(65, 90);
        login.setSize(150, 30);

        passwordLabel = new JLabel("Пароль: ");
        passwordLabel.setLocation(113, 100);
        passwordLabel.setSize(75, 150);

        password = new JPasswordField();
        password.setLocation(65, 190);
        password.setSize(150, 30);

        ok = new JButton("Войти");
        ok.setLocation(90, 260);
        ok.setSize(100, 30);

        panel = new JPanel();
        panel.setLayout(null);
        panel.add(loginLabel);
        panel.add(login);
        panel.add(password);
        panel.add(passwordLabel);
        panel.add(ok);
        panel.add(authLabel);

        add(panel);
    }
}
