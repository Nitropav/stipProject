package kursClient.view.editUserFrames;


import kursClient.entities.Faculty;
import kursClient.entities.Specialty;
import kursClient.entities.Student;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class AddStudentFrame extends JFrame {

    private JButton backButton;
    private JButton addButton;
    private JPanel panel;

    private JLabel addLabel;
    private JLabel nameLabel;
    private JLabel groupLabel;
    private JLabel typeLabel;
    private JLabel specialtyLabel;
    private JLabel facultyLabel;

    private JMenuItem saveSportsmenItem;

    private JTextField fullName;
    private JTextField group;
    private JComboBox<String> scType;
    private JComboBox<String> specialtyBox;
    private JComboBox<String> facultyBox;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;
    private List<Specialty> specialties;
    private List<Faculty> faculties;


    public AddStudentFrame(JFrame parent, Socket socket,
                          ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                          List<Student> students) {
        super("Booker: adding");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.students = students;
        parent.setVisible(false);
        init();

        addButton.addActionListener(event -> {
            String fullNameText = fullName.getText();
            String groupText = group.getText();
            String typeItem = (String) scType.getSelectedItem();
            String specialtyItem = (String) specialtyBox.getSelectedItem();
            String facultyItem = (String) facultyBox.getSelectedItem();

            if (fullNameText.isEmpty() || groupText.isEmpty()
                    || typeItem.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Все поля должны быть заполнены");
                return;
            }
            if (!groupText.matches("[0-9]+")){
                JOptionPane.showMessageDialog(this, "В номере группы должны быть только цифры");
                return;
            }
            else if (!fullNameText.matches("[А-Я]+")){
                JOptionPane.showMessageDialog(this, "В ФИО должны быть только буквы");
                return;
            }

            int maxInd = 0;
            for(int i = 0; i < students.size(); i++){
                if(students.get(i).getFullName().equals(fullNameText)){
                    JOptionPane.showMessageDialog(this, "this Name is already taken");
                    return;
                }
                if(students.get(i).getID() > maxInd){
                    maxInd = students.get(i).getID();
                }
            }

            Student newStudent = new Student(++maxInd, fullNameText, groupText, typeItem, getSpecId(specialtyItem), getFacId(facultyItem),1);


            try {
                objectOutputStream.writeObject("addStudent");
                objectOutputStream.writeObject(newStudent);
                newStudent.setSpecialty(specialtyItem);
                newStudent.setFacultyName(facultyItem);
                students.add(newStudent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Новый студент добавлен" );
            fullName.setText("");
            group.setText("");
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

    }

    private int getSpecId(String str){
        for(Specialty specialty: specialties){
            if(specialty.getSpecialtyName().equals(str)){
                return specialty.getID();
            }
        }
        return 1;
    }

    private int getFacId(String str){
        for(Faculty faculty: faculties){
            if(faculty.getFacultyName().equals(str)){
                return faculty.getID();
            }
        }
        return 1;
    }

    private void init() {

        loadAllSpecilitiesFromServer();
        loadAllFacultiesFromServer();
        String[] scItems = {
                "none",
                "social",
                "academic",
                "nominal",
        };

        String[] specialtyItems = new String[specialties.size()];
        for(int i = 0; i < specialties.size(); i++){
            specialtyItems[i] = specialties.get(i).getSpecialtyName();
        }

        String[] facultyItems = new String[faculties.size()];
        for(int i = 0; i < faculties.size(); i++){
            facultyItems[i] = faculties.get(i).getFacultyName();
        }



        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        fullName = new JTextField();
        fullName.setLocation(275, 70);
        fullName.setSize(150, 30);

        group = new JTextField();
        group.setLocation(275, 120);
        group.setSize(150, 30);

        scType = new JComboBox<>(scItems);
        scType.setLocation(275, 170);
        scType.setSize(150, 30);

        specialtyBox = new JComboBox<>(specialtyItems);
        specialtyBox.setLocation(275, 220);
        specialtyBox.setSize(150, 30);

        facultyBox = new JComboBox<>(facultyItems);
        facultyBox.setLocation(275, 270);
        facultyBox.setSize(150, 30);

        addLabel = new JLabel("Добавление студента");
        addLabel.setFont(new Font("Serif", Font.BOLD, 20));
        addLabel.setLocation(233, 10);
        addLabel.setSize(250, 50);

        nameLabel = new JLabel("Полное имя");
        nameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        nameLabel.setLocation(130, 60);
        nameLabel.setSize(140, 50);

        groupLabel = new JLabel("Группа");
        groupLabel.setFont(new Font("Serif", Font.BOLD, 20));
        groupLabel.setLocation(130, 110);
        groupLabel.setSize(140, 50);

        typeLabel = new JLabel("Вид стипендии");
        typeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        typeLabel.setLocation(130, 160);
        typeLabel.setSize(170, 50);

        specialtyLabel = new JLabel("Специальность");
        specialtyLabel.setFont(new Font("Serif", Font.BOLD, 20));
        specialtyLabel.setLocation(130, 210);
        specialtyLabel.setSize(140, 50);

        facultyLabel = new JLabel("Факультет");
        facultyLabel.setFont(new Font("Serif", Font.BOLD, 20));
        facultyLabel.setLocation(130, 260);
        facultyLabel.setSize(140, 50);


        backButton = new JButton("Назад");
        backButton.setLocation(460, 490);
        backButton.setSize(100, 40);

        addButton = new JButton("Добавить студента");
        addButton.setLocation(225, 390);
        addButton.setSize(230, 40);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(addButton);
        panel.add(addLabel);
        panel.add(nameLabel);
        panel.add(groupLabel);
        panel.add(typeLabel);
        panel.add(group);
        panel.add(fullName);
        panel.add(scType);
        panel.add(specialtyLabel);
        panel.add(specialtyBox);
        panel.add(facultyBox);
        panel.add(facultyLabel);

        add(panel);
    }

    private void loadAllSpecilitiesFromServer() {
        try {
            objectOutputStream.writeObject("getSpecialties");
            objectOutputStream.writeObject(null);
            specialties = (List<Specialty>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllFacultiesFromServer() {
        try {
            objectOutputStream.writeObject("getFaculties");
            objectOutputStream.writeObject(null);
            faculties = (List<Faculty>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
