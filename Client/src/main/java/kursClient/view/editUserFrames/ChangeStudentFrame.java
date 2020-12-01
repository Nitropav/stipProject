package kursClient.view.editUserFrames;

import kursClient.entities.Faculty;
import kursClient.entities.Specialty;
import kursClient.entities.Student;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ChangeStudentFrame extends JFrame {
    private JLabel menuStudentLabel;

    private JLabel loginLabel;
    private JLabel passwordLabel;
    private JLabel roleLabel;
    private JLabel specialtyLabel;
    private JLabel facultyLabel;

    private JTextField nameField;
    private JTextField groupField;

    private JComboBox<String> typeBox;
    private JComboBox<String> specialtyBox;
    private JComboBox<String> facultyBox;


    private JLabel userLabel;
    private JList<String> userList;
    private JScrollPane scrollPaneStudent;
    private DefaultListModel<String> listModelUser;

    private JButton editStudentButton;
    private JButton chooseUserButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;
    private List<Specialty> specialties;
    private List<Faculty> faculties;

    private Student currentStudent;


    public ChangeStudentFrame(JFrame parent, Socket socket,
                             ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                             List<Student> students) {
        super("Booker: changing students");
        setSize(1150, 590);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.students = students;


        parent.setVisible(false);

        init();

        chooseUserButton.addActionListener(e -> {
            int selectedIndex = userList.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Choose the student");
                return;
            }

            currentStudent = this.students.get(selectedIndex);

            nameField.setText(currentStudent.getFullName());
            groupField.setText(currentStudent.getStudentGroup());
            typeBox.setSelectedItem(currentStudent.getSpecialType());
            specialtyBox.setSelectedItem(currentStudent.getSpecialty());
            facultyBox.setSelectedItem(currentStudent.getFacultyName());

        });

        editStudentButton.addActionListener(e -> {
            if (currentStudent == null) {
                JOptionPane.showMessageDialog(this, "The user wasn't chosen");
                return;
            }
            String currentName = nameField.getText();
            String currentGroup = groupField.getText();
            String currentType = (String) typeBox.getSelectedItem();
            String currentSpecialty = (String) specialtyBox.getSelectedItem();
            String currentFaculty = (String) facultyBox.getSelectedItem();


            if (currentName.isEmpty() || currentGroup.isEmpty()
            ) {
                JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            for (Student student : this.students) {
                if (currentStudent.equals(student)) {
                    continue;
                }
                if ((student.getFullName().equals(currentName)
                        && student.getStudentGroup().equals(currentGroup))) {
                    JOptionPane.showMessageDialog(this, "This user already exists");
                    return;
                }
            }

            currentStudent.setFullName(currentName);
            currentStudent.setStudentGroup(currentGroup);
            currentStudent.setSpecialType(currentType);
            currentStudent.setSpecialty(currentSpecialty);
            currentStudent.setFacultyName(currentFaculty);
            currentStudent.setIdSpeciality(getSpecId(currentSpecialty));
            currentStudent.setIdFaculty(getFacId(currentFaculty));

            try {
                objectOutputStream.writeObject("setStudent");
                objectOutputStream.writeObject(currentStudent);
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            listModelUser.clear();
            readStudents();

            JOptionPane.showMessageDialog(this, "Редактирование успешное!");
            nameField.setText("");
            groupField.setText("");
            typeBox.setSelectedIndex(0);
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

        String[] typeItems = {
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

        listModelUser = new DefaultListModel<>();
        userList = new JList<>(listModelUser);
        userList.setLayoutOrientation(JList.VERTICAL);
        //////////////////////////////////////////////////////////////////
        userLabel = new JLabel("Изменить данные студента: ");
        userLabel.setLocation(200, 10);
        userLabel.setSize(200, 50);

        ///////////////////////////////////////////////////active buttons
        backButton = new JButton("Назад");
        backButton.setLocation(10, 480);
        backButton.setSize(80, 30);

        chooseUserButton = new JButton("Выбрать");
        chooseUserButton.setLocation(670, 480);
        chooseUserButton.setSize(110, 30);

        readStudents();

        scrollPaneStudent = new JScrollPane(userList);
        scrollPaneStudent.setLocation(50, 50);
        scrollPaneStudent.setSize(700, 400);


        menuStudentLabel = new JLabel("Изменить студента: ");
        menuStudentLabel.setLocation(500, 10);
        menuStudentLabel.setSize(200, 50);

        loginLabel = new JLabel("Полное имя: ");
        loginLabel.setLocation(800, 70);
        loginLabel.setSize(80, 50);

        passwordLabel = new JLabel("Группа: ");
        passwordLabel.setLocation(800, 120);
        passwordLabel.setSize(80, 50);

        roleLabel = new JLabel("Стипендия: ");
        roleLabel.setLocation(800, 170);
        roleLabel.setSize(80, 50);

        specialtyLabel = new JLabel("Специальность: ");
        specialtyLabel.setLocation(800, 220);
        specialtyLabel.setSize(80, 50);

        facultyLabel = new JLabel("Факультет: ");
        facultyLabel.setLocation(800, 270);
        facultyLabel.setSize(80, 50);

        editStudentButton = new JButton("Изменить");
        editStudentButton.setLocation(1000, 480);
        editStudentButton.setSize(110, 30);

        nameField = new JTextField();
        nameField.setLocation(890, 80);
        nameField.setSize(150, 30);

        groupField = new JTextField();
        groupField.setLocation(890, 130);
        groupField.setSize(150, 30);



        typeBox = new JComboBox<>(typeItems);
        typeBox.setLocation(890, 180);
        typeBox.setSize(150, 30);

        specialtyBox = new JComboBox<>(specialtyItems);
        specialtyBox.setLocation(890, 230);
        specialtyBox.setSize(150, 30);

        facultyBox = new JComboBox<>(facultyItems);
        facultyBox.setLocation(890, 280);
        facultyBox.setSize(150, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);
        panel.add(chooseUserButton);
        panel.add(scrollPaneStudent);

        panel.add(menuStudentLabel);
        panel.add(loginLabel);
        panel.add(passwordLabel);
        panel.add(roleLabel);

        panel.add(nameField);
        panel.add(groupField);

        panel.add(typeBox);
        panel.add(editStudentButton);
        panel.add(specialtyBox);
        panel.add(specialtyLabel);
        panel.add(facultyBox);
        panel.add(facultyLabel);

        add(panel);
    }

    private void readStudents() {
        for (Student student : students) {
            listModelUser.addElement("Name - " + student.getFullName() + " Group - " +
                    student.getStudentGroup() + " Scholarship type - " +
                    student.getSpecialType() + " Specialty - "
                    + student.getSpecialty() + " Faculty - " + student.getFacultyName());
        }
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
