package kursClient.view.AdminFrames;

import kursClient.entities.Admin;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class AdminShowAccountants extends JFrame {
    private JPanel panel;
    private JButton backButton;
    private JTable table;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Admin> admins;

    public AdminShowAccountants(JFrame parent, Socket socket,
                                   ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                   List<Admin> admins) {

        super("Accountants");
        setSize(830, 460);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.admins = admins;

        parent.setVisible(false);

        init();

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

    }

    private void init() {
        panel = new JPanel();
        panel.setLayout(null);

        backButton = new JButton("Back");
        backButton.setLocation(10, 380);
        backButton.setSize(80, 30);

        JLabel title = new JLabel("Accountants");
        title.setLocation(355, 10);
        title.setSize(120, 30);

        table = new JTable(getData(), getHeaders());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setLocation(10, 50);
        scrollPane.setSize(790, 300);

        panel.add(backButton);
        panel.add(scrollPane);
        panel.add(title);

        add(panel);
    }

    private String[][] getData() {
        ArrayList<Admin> users = new ArrayList<Admin>();
        for(int i = 0; i < admins.size();i++ ){
            if(admins.get(i).getRole().equals("booker")){
                users.add(admins.get(i));
            }
        }
        String[][] strings = new String[users.size()][5];
        for (int i = 0; i < strings.length; i++) {
            int j = i + 1;
            strings[i][0] = String.valueOf(j);
            strings[i][1] = users.get(i).getLogin();
            strings[i][2] = users.get(i).getPassword();
            strings[i][3] = users.get(i).getRole();
            strings[i][4] = String.valueOf(users.get(i).getID());
        }

        return strings;
    }

    private String[] getHeaders() {
        String[] strings = {"#", "LOGIN", "PASSWORD", "ROLE", "ID IN DATABASE"};
        return strings;
    }
}
