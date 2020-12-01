package kursClient.view.AdminFrames;

import kursClient.entities.Admin;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MainAdminShowWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton showAdmins;
    private JButton showAccountants;

    private JButton backButton;
    private JPanel panel;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Admin> users;


    public MainAdminShowWindow(JFrame parent, Socket socket,
                           ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                           List<Admin> users) {
        super("Admin: view");
        setSize(350, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.users = users;

        parent.setVisible(false);

        init();

        showAccountants.addActionListener(event -> {
            AdminShowAccountants adminShowAccountants = new AdminShowAccountants(this, socket, objectOutputStream, objectInputStream, users);
            adminShowAccountants.setVisible(true);
            adminShowAccountants.setLocationRelativeTo(null);
        });

        showAdmins.addActionListener(event -> {
            ShowAdminsWindow showAdminsWindow = new ShowAdminsWindow(this, socket, objectOutputStream, objectInputStream, this.users);
            showAdminsWindow.setVisible(true);
            showAdminsWindow.setLocationRelativeTo(null);
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });


    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);



        menuAdminLabel = new JLabel("Просмотр: ");
        menuAdminLabel.setLocation(100, 10);
        menuAdminLabel.setSize(100, 50);

        showAdmins = new JButton("Просмотреть всех администраторов");
        showAdmins.setLocation(35, 70);
        showAdmins.setSize(260, 30);

        showAccountants = new JButton("Просмотреть всех бухгалтеров");
        showAccountants.setLocation(35, 120);
        showAccountants.setSize(260, 30);


        backButton = new JButton("Назад");
        backButton.setLocation(10, 220);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);


        panel.add(backButton);
//        panel.add(showPerformanceButton);
        panel.add(showAccountants);
        panel.add(showAdmins);
        panel.add(menuAdminLabel);

        add(panel);
    }
}
