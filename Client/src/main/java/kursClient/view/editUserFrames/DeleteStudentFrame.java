package kursClient.view.editUserFrames;



import kursClient.entities.Student;


import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class DeleteStudentFrame extends JFrame {

    private JLabel adminLabel;
    private JList<String> adminList;
    private JScrollPane scrollPaneStudent;
    private DefaultListModel<String> listModelAdmin;

    private JButton removeAdminButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;



    public DeleteStudentFrame(JFrame parent, Socket socket,
                             ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                             List<Student> students) {
        super("Booker: deleting students");
        setSize(600, 470);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.students = students;


        parent.setVisible(false);

        init();

        removeAdminButton.addActionListener(e -> {
            int selectedIndex = adminList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Note the student");
                return;
            }

            Student chosenStudent = students.get(selectedIndex);

            int answer = JOptionPane.showConfirmDialog(this,
                    "Вы уверены, что хотите удалить студента:\n" +
                            "\nИмя: " + chosenStudent.getFullName() + " " +
                            "\nГруппа: " + chosenStudent.getStudentGroup());
            if (answer == 0) {
                students.remove(chosenStudent);
                try {
                    objectOutputStream.writeObject("deleteStudent");
                    objectOutputStream.writeObject(chosenStudent);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            listModelAdmin.clear();
            readStudent();
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
        adminLabel = new JLabel("Deletion of student: ");
        adminLabel.setLocation(200, 10);
        adminLabel.setSize(200, 50);

        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 380);
        backButton.setSize(80, 30);

        removeAdminButton = new JButton("Удалить");
        removeAdminButton.setLocation(470, 380);
        removeAdminButton.setSize(110, 30);

        readStudent();

        scrollPaneStudent = new JScrollPane(adminList);
        scrollPaneStudent.setLocation(50, 50);
        scrollPaneStudent.setSize(500, 200);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(removeAdminButton);
        panel.add(scrollPaneStudent);


        add(panel);
    }

    private void readStudent() {

        for (Student student : students) {
            listModelAdmin.addElement("Name - " + student.getFullName() + " Group - " +
                    student.getStudentGroup() + " Specialty - " +
                    student.getSpecialty() + " Faculty - " +
                    student.getFacultyName());
        }
    }
}
