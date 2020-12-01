package kursClient.view.AdminFrames;

import kursClient.entities.Admin;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminChangeWindow extends JFrame {
    private JLabel menuAdminLabel;

    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JLabel roleLabel;
    private JLabel cityLabel;

    private JTextField loginField;
    private JTextField passwordField;

    private JComboBox<String> sportsBox;


    private JLabel userLabel;
    private JList<String> userList;
    private JScrollPane scrollPaneUser;
    private DefaultListModel<String> listModelUser;

    private JButton editUserButton;
    private JButton chooseUserButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Admin> admins;


    private Admin currentAdmin;


    public AdminChangeWindow(JFrame parent, Socket socket,
                                  ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                  List<Admin> admins) {
        super("Admin: changing users");
        setSize(750, 590);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.admins = admins;


        parent.setVisible(false);

        init();

        chooseUserButton.addActionListener(e -> {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Choose the user");
                return;
            }

            currentAdmin = this.admins.get(selectedIndex);

            loginField.setText(currentAdmin.getLogin());
            passwordField.setText(currentAdmin.getPassword());
            sportsBox.setSelectedItem(currentAdmin.getRole());

        });

        editUserButton.addActionListener(e -> {
            if (currentAdmin == null) {
                JOptionPane.showMessageDialog(this, "The user wasn't chosen");
                return;
            }
            String currentName = loginField.getText();
            String currentSurname = passwordField.getText();

            String currentSport = (String) sportsBox.getSelectedItem();


            if (currentName.isEmpty() || currentSurname.isEmpty()
                    ) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            for (Admin admin : this.admins) {
                if (currentAdmin.equals(admin)) {
                    continue;
                }
                if ((admin.getLogin().equals(currentName)
                        && admin.getPassword().equals(currentSurname))) {
                    JOptionPane.showMessageDialog(this, "This user already exists");
                    return;
                }
            }


            currentAdmin.setLogin(currentName);
            currentAdmin.setPassword(currentSurname);
            currentAdmin.setRole(currentSport);


            listModelUser.clear();
            readTeacher();
            try {
                objectOutputStream.writeObject("setAdmin");
                objectOutputStream.writeObject(currentAdmin);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Изменения прошли успешно!");
            loginField.setText("");
            passwordField.setText("");
            sportsBox.setSelectedIndex(0);
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        listModelUser = new DefaultListModel<>();
        userList = new JList<>(listModelUser);
        userList.setLayoutOrientation(JList.VERTICAL);
        //////////////////////////////////////////////////////////////////
        userLabel = new JLabel("Изменение пользователя: ");
        userLabel.setLocation(100, 10);
        userLabel.setSize(200, 50);

        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 480);
        backButton.setSize(80, 30);

        chooseUserButton = new JButton("Выбрать");
        chooseUserButton.setLocation(270, 480);
        chooseUserButton.setSize(110, 30);

        readTeacher();

        scrollPaneUser = new JScrollPane(userList);
        scrollPaneUser.setLocation(50, 50);
        scrollPaneUser.setSize(300, 400);


        menuAdminLabel = new JLabel("Изменение пользователя: ");
        menuAdminLabel.setLocation(500, 10);
        menuAdminLabel.setSize(200, 50);

        loginLabel = new JLabel("Логин: ");
        loginLabel.setLocation(400, 70);
        loginLabel.setSize(80, 50);

        passwordLabel = new JLabel("Пароль: ");
        passwordLabel.setLocation(400, 120);
        passwordLabel.setSize(80, 50);

        roleLabel = new JLabel("Роль: ");
        roleLabel.setLocation(400, 170);
        roleLabel.setSize(80, 50);

        editUserButton = new JButton("Изменить");
        editUserButton.setLocation(600, 480);
        editUserButton.setSize(110, 30);

        loginField = new JTextField();
        loginField.setLocation(490, 80);
        loginField.setSize(150, 30);

        passwordField = new JTextField();
        passwordField.setLocation(490, 130);
        passwordField.setSize(150, 30);


        String[] items = {
                "admin",
                "booker"
        };
        sportsBox = new JComboBox<>(items);
        sportsBox.setLocation(490, 180);
        sportsBox.setSize(150, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(chooseUserButton);
        panel.add(scrollPaneUser);

        panel.add(menuAdminLabel);
        panel.add(loginLabel);
        panel.add(passwordLabel);
        panel.add(roleLabel);

        panel.add(loginField);
        panel.add(passwordField);

        panel.add(sportsBox);
        panel.add(editUserButton);

        add(panel);
    }

    private void readTeacher() {
        for (Admin admin : admins) {
            listModelUser.addElement("Login - " + admin.getLogin() + " Password - " +
                    admin.getPassword() + " Role - " +
                    admin.getRole());
        }
    }
}
