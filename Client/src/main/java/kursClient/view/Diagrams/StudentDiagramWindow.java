package kursClient.view.Diagrams;


import kursClient.entities.Student;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class StudentDiagramWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton byPaymentButton;
    private JButton byCountryTeacherButton;
    private JButton bySportSportsmenButton;
    private JButton byAverageMarkButton;
    private JButton backButton;
    private JPanel panel;

    private JMenuItem saveSportsmenItem;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;
//    private List<Student> sportsmen;
    private Student entryTeacher;


    public StudentDiagramWindow(JFrame parent, Socket socket,
                              ObjectOutputStream objectOutputStream,
                              ObjectInputStream objectInputStream,
                              List<Student> students) {
        super("Диаграммы");
        setSize(300, 380);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.students = students;
//        this.referees = referees;
        this.entryTeacher = entryTeacher;

        parent.setVisible(false);

        init();

        byPaymentButton.addActionListener(event -> {
            StudentDiagramByPayment studentDiagramByPayment =
                    new StudentDiagramByPayment(this, socket,
                            objectOutputStream, objectInputStream,
                            students);
            studentDiagramByPayment.setVisible(true);
            studentDiagramByPayment.setLocationRelativeTo(null);
        });

        byAverageMarkButton.addActionListener(event -> {
            StudentDiagramByFaculty studentDiagramByFaculty =
                    new StudentDiagramByFaculty(this, socket,
                            objectOutputStream, objectInputStream,
                            students);
            studentDiagramByFaculty.setVisible(true);
            studentDiagramByFaculty.setLocationRelativeTo(null);
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuAdminLabel = new JLabel("Диаграмма ");
        menuAdminLabel.setLocation(107, 10);
        menuAdminLabel.setSize(75, 50);

        byPaymentButton = new JButton("По общей стипендии");
        byPaymentButton.setLocation(50, 70);
        byPaymentButton.setSize(180, 30);

        byAverageMarkButton = new JButton("По средней оценке");
        byAverageMarkButton.setLocation(50, 120);
        byAverageMarkButton.setSize(180, 30);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 270);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(byPaymentButton);
        panel.add(backButton);
        panel.add(menuAdminLabel);
        panel.add(byAverageMarkButton);

        add(panel);
    }
}
