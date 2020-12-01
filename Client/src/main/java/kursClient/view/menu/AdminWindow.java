package kursClient.view.menu;

import kursClient.entities.Admin;
import kursClient.view.AdminFrames.AdminAddWindow;
import kursClient.view.AdminFrames.AdminChangeWindow;
import kursClient.view.AdminFrames.AdminDeleteWindow;
import kursClient.view.AdminFrames.MainAdminShowWindow;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminWindow extends JFrame {
    private JLabel menuAdminLabel;
    private JButton addButton;
    //private JButton changeButton;
    private JButton changeButton;
    private JButton showButton;
    private JButton backButton;
    private JPanel panel;
    private JButton deleteButton;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Admin> users;
    //  private List<Sportsman> sportsmen;

    public AdminWindow(JFrame parent, Socket socket,
                           ObjectOutputStream objectOutputStream,
                           ObjectInputStream objectInputStream) {
        super("Admin: menu");
        setSize(400, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        parent.setVisible(false);

        init();

        addButton.addActionListener(event -> {
            AdminAddWindow adminAddWindow =
                    new AdminAddWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            users);
            adminAddWindow.setVisible(true);
            adminAddWindow.setLocationRelativeTo(null);
        });

        changeButton.addActionListener(event -> {
            AdminChangeWindow adminChangeWindow =
                    new AdminChangeWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            users);
            adminChangeWindow.setVisible(true);
            adminChangeWindow.setLocationRelativeTo(null);
        });

        deleteButton.addActionListener(event -> {
            AdminDeleteWindow adminDeleteWindow =
                    new AdminDeleteWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            users);
            adminDeleteWindow.setVisible(true);
            adminDeleteWindow.setLocationRelativeTo(null);
        });

        showButton.addActionListener(event -> {
            MainAdminShowWindow adminShowWindow = new MainAdminShowWindow(this, socket, objectOutputStream, objectInputStream, users);
            adminShowWindow.setVisible(true);
            adminShowWindow.setLocationRelativeTo(null);
        });


        backButton.addActionListener(event -> {this.dispose();
            parent.setVisible(true);
        });



          loadAllUsersFromServer();
    }

    private void loadAllUsersFromServer() {
        try {
            objectOutputStream.writeObject("getAllUsers");
            objectOutputStream.writeObject(null);
            users = (List<Admin>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuAdminLabel = new JLabel("Функции администратора: ");
        menuAdminLabel.setLocation(125, 10);
        menuAdminLabel.setSize(240, 50);

        addButton = new JButton("Добавить пользователя");
        addButton.setLocation(75, 70);
        addButton.setSize(240, 30);

        deleteButton = new JButton("Удалить пользователя");
        deleteButton.setLocation(75, 120);
        deleteButton.setSize(240, 30);



        changeButton = new JButton("Редактировать пользователя");
        changeButton.setLocation(75, 170);
        changeButton.setSize(240, 30);

        showButton = new JButton("Просмотр");
        showButton.setLocation(75, 220);
        showButton.setSize(240, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(150, 270);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(changeButton);
        panel.add(backButton);
        panel.add(deleteButton);
        panel.add(addButton);
        panel.add(menuAdminLabel);
        panel.add(showButton);

        add(panel);
    }
}
