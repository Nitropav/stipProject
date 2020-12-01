package kursClient.view.AdminFrames;


import kursClient.entities.Admin;


import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AdminDeleteWindow extends JFrame {

    private JLabel adminLabel;
    private JList<String> adminList;
    private JScrollPane scrollPaneAdmin;
    private DefaultListModel<String> listModelAdmin;

    private JButton removeAdminButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Admin> admins;



    public AdminDeleteWindow(JFrame parent, Socket socket,
                                    ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                    List<Admin> admins) {
        super("Admin: deleting users");
        setSize(400, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.admins = admins;


        parent.setVisible(false);

        init();

        removeAdminButton.addActionListener(e -> {
            int selectedIndex = adminList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Note the user");
                return;
            }

            Admin chosenAdmin = admins.get(selectedIndex);

            int answer = JOptionPane.showConfirmDialog(this,
                    "Уверены, что хотите удалить пользователя:\n" +
                            "\nЛогин: " + chosenAdmin.getLogin() + " " +
                            "\nПароль: " + chosenAdmin.getPassword() +
                            "\nРоль: " + chosenAdmin.getRole());
            if (answer == 0) {
                admins.remove(chosenAdmin);
                try {
                    objectOutputStream.writeObject("deleteUser");
                    objectOutputStream.writeObject(chosenAdmin);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            listModelAdmin.clear();
            readTeacher();
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        listModelAdmin = new DefaultListModel<>();
        adminList = new JList<>(listModelAdmin);
        adminList.setLayoutOrientation(JList.VERTICAL);
        //////////////////////////////////////////////////////////////////
        adminLabel = new JLabel("Удаление пользователя: ");
        adminLabel.setLocation(100, 10);
        adminLabel.setSize(200, 50);

        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 380);
        backButton.setSize(80, 30);

        removeAdminButton = new JButton("Удалить");
        removeAdminButton.setLocation(270, 380);
        removeAdminButton.setSize(110, 30);

        readTeacher();

        scrollPaneAdmin = new JScrollPane(adminList);
        scrollPaneAdmin.setLocation(50, 50);
        scrollPaneAdmin.setSize(300, 200);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(removeAdminButton);
        panel.add(scrollPaneAdmin);


        add(panel);
    }

    private void readTeacher() {

        for (Admin admin : admins) {
            listModelAdmin.addElement("Login - " + admin.getLogin() + " Password - " +
                    admin.getPassword() + " Role - " +
                    admin.getRole());
        }
    }
}
