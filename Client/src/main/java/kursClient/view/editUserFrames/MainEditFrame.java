package kursClient.view.editUserFrames;


import kursClient.entities.Student;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MainEditFrame extends JFrame {

    private JLabel menuBookerLabel;
    private JButton addStudent;
    private JButton editStudent;
    private JButton deleteStudent;

    private JButton backButton;
    private JPanel panel;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;


    public MainEditFrame(JFrame parent, Socket socket,
                               ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                               List<Student> students) {
        super("Booker: edit menu");
        setSize(300, 350);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.students = students;

        parent.setVisible(false);

        init();

        editStudent.addActionListener(event -> {
            ChangeStudentFrame changeStudentFrame = new ChangeStudentFrame(this, socket, objectOutputStream, objectInputStream, students);
            changeStudentFrame.setVisible(true);
            changeStudentFrame.setLocationRelativeTo(null);
        });

        addStudent.addActionListener(event -> {
            AddStudentFrame addStudentFrame = new AddStudentFrame(this, socket, objectOutputStream, objectInputStream, this.students);
            addStudentFrame.setVisible(true);
            addStudentFrame.setLocationRelativeTo(null);
        });


        deleteStudent.addActionListener(event -> {
            DeleteStudentFrame deleteStudentFrame = new DeleteStudentFrame(this, socket, objectOutputStream, objectInputStream, this.students);
            deleteStudentFrame.setVisible(true);
            deleteStudentFrame.setLocationRelativeTo(null);
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });


    }

    private void init() {
        menuBookerLabel = new JLabel("Меню редактирования: ");
        menuBookerLabel.setLocation(80, 10);
        menuBookerLabel.setSize(190, 50);

        addStudent = new JButton("Добавить студента");
        addStudent.setLocation(35, 70);
        addStudent.setSize(210, 30);

        editStudent = new JButton("Редактировать студента");
        editStudent.setLocation(35, 120);
        editStudent.setSize(210, 30);

        deleteStudent = new JButton("Удалить студента");
        deleteStudent.setLocation(35, 170);
        deleteStudent.setSize(210, 30);


        backButton = new JButton("Назад");
        backButton.setLocation(10, 260);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);


        panel.add(backButton);

        panel.add(editStudent);
        panel.add(addStudent);
        panel.add(deleteStudent);
        panel.add(menuBookerLabel);

        add(panel);
    }
}
