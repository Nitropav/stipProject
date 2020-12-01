package kursClient.view.Diagrams;

import kursClient.entities.Student;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;


import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class StudentDiagramByFaculty extends JFrame {

    private JButton backButton;
    private JPanel panel;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;
    private Student studentsTeacher;


    public StudentDiagramByFaculty(JFrame parent, Socket socket,
                                           ObjectOutputStream objectOutputStream,
                                           ObjectInputStream objectInputStream,
                                          List<Student> students
    ) {
        super("Diagram");
        setSize(500, 550);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        System.out.println(students);
        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
        this.students = students;

        parent.setVisible(false);

        init();


        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });
    }


    private void init() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JFreeChart chart = ChartFactory.createBarChart("Средняя оценка", "Факультеты", "Средняя оценка", createDataset(),
                PlotOrientation.VERTICAL, true, true, false);

        ChartPanel chartPanel = new ChartPanel(chart);
        getContentPane().add(chartPanel);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 420);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(backButton);


        chartPanel.setLocation(50, 50);
        chartPanel.setSize(350, 350);
        panel.add(chartPanel);

//        panel.add(jfxPanel);

        add(panel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        float FKP = 0;
        int fkpCount = 0;
        float FKSIS = 0;
        int fksisCount = 0;
        float FITU = 0;
        int fituCount = 0;
        float FRE = 0;
        int freCount = 0;
        for(Student student: students){
            if(student.getMarks() == null){
                continue;
            }
            switch (student.getFacultyName()){
                case "ФКП": FKP = FKP + countAveargeMark(student); ++fkpCount; break;
                case "ФКСИС": FKSIS = FKSIS + countAveargeMark(student); ++fksisCount; break;
                case "ФИТУ": FITU = FITU + countAveargeMark(student); ++fituCount; break;
                case "ФРЭ": FRE = FRE + countAveargeMark(student); ++freCount; break;
            }
        }
        dataset.setValue(FKP / fkpCount, "ФКП", "Average mark");
        dataset.setValue(FITU/fituCount, "ФИТУ", "Average mark");
        dataset.setValue(FKSIS/fksisCount, "ФКСИС", "Average mark");
        dataset.setValue(FRE/freCount, "ФРЭ", "Average mark");
        return dataset;
    }
    private float countAveargeMark(Student student){
        char[] strToArray = student.getMarks().toCharArray();
        float sum = 0;
        for(int i = 0; i < strToArray.length; i++) {
            sum += Character.getNumericValue(strToArray[i]);
        }
        sum = sum / strToArray.length;

        return sum;
    }

}
