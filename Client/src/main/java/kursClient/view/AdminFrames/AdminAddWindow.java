package kursClient.view.AdminFrames;

import kursClient.entities.Admin;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminAddWindow extends JFrame {

    private JButton backButton;
    private JButton addButton;
    private JPanel panel;

    private JLabel addLabel;
    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JLabel roleLabel;

    private JMenuItem saveSportsmenItem;

    private JTextField login;
    private JTextField password;

    private JComboBox<String> role;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Admin> admins;



    public AdminAddWindow(JFrame parent, Socket socket,
                          ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                          List<Admin> admins) {
        super("Admin: adding");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.admins = admins;
        parent.setVisible(false);
        init();

        addButton.addActionListener(event -> {
            String loginName = login.getText();
            String passwordValue = password.getText();
            String roleValue = (String) role.getSelectedItem();
            System.out.println("login " + loginName + " password+ " + passwordValue + " role " + roleValue);
            if (loginName.isEmpty() || passwordValue.isEmpty()
                    || roleValue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }
            int maxInd = 0;
            for(int i = 0; i < admins.size(); i++){
                if(admins.get(i).getLogin().equals(loginName)){
                    JOptionPane.showMessageDialog(this, "Такой пользователь уже существует");
                    return;
                }
                if(admins.get(i).getID() > maxInd){
                    maxInd = admins.get(i).getID();
                }
            }

            Admin newUser = new Admin(++maxInd, loginName, passwordValue, roleValue);
            admins.add(newUser);

            try {
                objectOutputStream.writeObject("addUser");
                objectOutputStream.writeObject(newUser);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Новый " + roleValue + " добавлен" );
            login.setText("");
            password.setText("");
        });


        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

    }


    private void init() {
        String[] items = {
                "admin",
                "booker"
        };

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        login = new JTextField();
        login.setLocation(225, 90);
        login.setSize(150, 30);

        password = new JTextField();
        password.setLocation(225, 190);
        password.setSize(150, 30);

        role = new JComboBox<>(items);
        role.setLocation(225, 290);
        role.setSize(150, 30);

        addLabel = new JLabel("Добавление пользователя:");
        addLabel.setFont(new Font("Serif", Font.BOLD, 20));
        addLabel.setLocation(193, 10);
        addLabel.setSize(260, 50);

        loginLabel = new JLabel("Логин");
        loginLabel.setFont(new Font("Serif", Font.BOLD, 20));
        loginLabel.setLocation(275, 50);
        loginLabel.setSize(140, 50);

        passwordLabel = new JLabel("Пароль");
        passwordLabel.setFont(new Font("Serif", Font.BOLD, 20));
        passwordLabel.setLocation(265, 150);
        passwordLabel.setSize(140, 50);

        roleLabel = new JLabel("Роль");
        roleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        roleLabel.setLocation(275, 250);
        roleLabel.setSize(140, 50);


        backButton = new JButton("Назад");
        backButton.setLocation(460, 490);
        backButton.setSize(100, 40);

        addButton = new JButton("Добавить пользователя");
        addButton.setLocation(205, 390);
        addButton.setSize(200, 40);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(addButton);
        panel.add(addLabel);
        panel.add(loginLabel);
        panel.add(passwordLabel);
        panel.add(roleLabel);
        panel.add(password);
        panel.add(login);
        panel.add(role);

        add(panel);
    }
}
