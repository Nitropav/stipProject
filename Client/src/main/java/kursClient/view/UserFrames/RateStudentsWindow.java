package kursClient.view.UserFrames;

import kursClient.entities.Student;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RateStudentsWindow extends JFrame {
    private JLabel menuAdminLabel;
    private JLabel rateLabel;
    private JTextField ratingField;

    private JLabel userLabel;
    private JLabel showLabel;
    private JList<String> userList;
    private JScrollPane scrollPaneUser;
    private DefaultListModel<String> listModelStudent;

    private JButton editStudentButton;
    private JButton chooseStudentButton;
    private JButton backButton;
    private JButton showAllStudents;
    private JButton showNotMarkedStudents;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;
    private Student currentStudent;

    public RateStudentsWindow(JFrame parent, Socket socket,
                             ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                             List<Student> students) {
        super("Booker: rate students");
        setSize(750, 650);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.students = students;


        parent.setVisible(false);

        init();

        chooseStudentButton.addActionListener(e -> {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Выбирите студента");
                return;
            }

            for (Student student : students) {
                if(student.getID() == findId(userList.getSelectedValue())){
                    currentStudent = student;
                }
            }

            ratingField.setText(currentStudent.getMarks());

        });

        showAllStudents.addActionListener(e -> {
            listModelStudent.clear();
            showRatedStudents();

        });

        showNotMarkedStudents.addActionListener(e -> {
            listModelStudent.clear();
            showNotRatedStudents();
        });

        editStudentButton.addActionListener(e -> {
            if (currentStudent == null) {
                JOptionPane.showMessageDialog(this, "Студент не выбран");
                return;
            }
            String currentRating = ratingField.getText();

            if (currentRating.isEmpty()
            ) {
                JOptionPane.showMessageDialog(this, "Это поле не заполнено");
                return;
            }
            if(currentRating.length() > 6){
                JOptionPane.showMessageDialog(this, "В этом поле не может быть больше 5 символов");
                return;
            }
            else if (!currentRating.matches("[0-9]+")){
                JOptionPane.showMessageDialog(this, "В это поле можно вводить только цифры");
                return;
            }

            currentStudent.setMarks(currentRating);

            try {
                objectOutputStream.writeObject("setMarks");
                objectOutputStream.writeObject(currentStudent);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            listModelStudent.clear();
            showNotRatedStudents();

            JOptionPane.showMessageDialog(this, "Оценка успешно добавлена");
            ratingField.setText("");

        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private int findId(String str){
        char[] strToArray = str.toCharArray();
        String res = "";
        for(int y = 0; y < 6; y++) {
            if(Character.isDigit(strToArray[y])) {
                res = res + strToArray[y];
            }
        }
        return Integer.parseInt(res);
    }

    private void init() {
        listModelStudent = new DefaultListModel<>();
        userList = new JList<>(listModelStudent);
        userList.setLayoutOrientation(JList.VERTICAL);

        userLabel = new JLabel("Оценка студента: ");
        userLabel.setLocation(100, 10);
        userLabel.setSize(200, 50);


        backButton = new JButton("Назад");
        backButton.setLocation(10, 555);
        backButton.setSize(110, 30);

        chooseStudentButton = new JButton("Выбрать");
        chooseStudentButton.setLocation(290, 515);
        chooseStudentButton.setSize(110, 30);

        showAllStudents = new JButton("Студенты с оценкой");
        showAllStudents.setLocation(520, 555);
        showAllStudents.setSize(200, 30);

        showNotMarkedStudents = new JButton("Студенты без оценки");
        showNotMarkedStudents.setLocation(520, 515);
        showNotMarkedStudents.setSize(200, 30);


        showNotRatedStudents();

        scrollPaneUser = new JScrollPane(userList);
        scrollPaneUser.setLocation(50, 50);
        scrollPaneUser.setSize(650, 350);


        menuAdminLabel = new JLabel("Оценка студента: ");
        menuAdminLabel.setLocation(305, 10);
        menuAdminLabel.setSize(140, 50);

        showLabel = new JLabel("Просмотр студентов");
        showLabel.setLocation(580, 475);
        showLabel.setSize(170, 30);

        rateLabel = new JLabel("Оценка");
        rateLabel.setLocation(325, 410);
        rateLabel.setSize(110, 30);

        ratingField = new JTextField();
        ratingField.setLocation(290, 450);
        ratingField.setSize(150, 30);


        editStudentButton = new JButton("Добавить");
        editStudentButton.setLocation(290, 555);
        editStudentButton.setSize(110, 30);


        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(chooseStudentButton);
        panel.add(scrollPaneUser);
        panel.add(showAllStudents);
        panel.add(showNotMarkedStudents);
        panel.add(menuAdminLabel);
        panel.add(rateLabel);
        panel.add(showLabel);
        panel.add(ratingField);
        panel.add(editStudentButton);

        add(panel);
    }

    private void showNotRatedStudents() {
        for (Student student : students) {
            if(student.getMarks() == null){
                listModelStudent.addElement("id " + student.getID() + " full name - " + student.getFullName() + " Group - " +
                        student.getStudentGroup() + " special scholarship - " +
                        student.getSpecialType() + " faculty - " +
                        student.getFacultyName() + " specialty - " +
                        student.getSpecialty());
            }
        }
        if (listModelStudent.isEmpty()){
            listModelStudent.addElement("Ничего не найдено");
        }
    }

    private void showRatedStudents() {
        for (Student student : students) {
            if(!parseMarks(student.getMarks()).equals("none")){
                listModelStudent.addElement("id " + student.getID() + " full name - " + student.getFullName() + " Group - " +
                        student.getStudentGroup() + " special scholarship - " +
                        student.getSpecialType() + " faculty - " +
                        student.getFacultyName()+ " specialty - " +
                        student.getSpecialty() + " Marks - " + parseMarks(student.getMarks()));
            }
        }
        if (listModelStudent.isEmpty()){
            listModelStudent.addElement("Ничего не найдено");
        }
    }

    private String parseMarks(String str){
        if(str == null){
            return "none";
        }
        char[] strToArray = str.toCharArray();
        String res = "";
        for(int y = 0; y < strToArray.length; y++) {
            res = res + strToArray[y] + " ";
        }
        return res;
    }
}





