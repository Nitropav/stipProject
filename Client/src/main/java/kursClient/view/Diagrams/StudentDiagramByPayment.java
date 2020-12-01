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

public class StudentDiagramByPayment extends JFrame {

    private JButton backButton;
    private JPanel panel;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;
    private Student studentsTeacher;


    public StudentDiagramByPayment(JFrame parent, Socket socket,
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
//        this.referees = referees;
//        this.entryTeacher = entryTeacher;

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

        JFreeChart chart = ChartFactory.createPieChart("Стипендия студентов", createDataset(),
                 true, true, false);


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

    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        float FKP = 0;
        float FKSIS = 0;
        float FITU = 0;
        float FRE = 0;
        for(Student student: students){
            if(student.getMarks() == null){
                continue;
            }
            switch (student.getFacultyName()){
                case "ФКП": FKP = FKP + student.getTotalPayment(); break;
                case "ФКСИС": FKSIS = FKSIS + student.getTotalPayment(); break;
                case "ФИТУ": FITU = FITU + student.getTotalPayment(); break;
                case "ФРЭ": FRE = FRE + student.getTotalPayment(); break;
            }
        }
        dataset.setValue("ФКП", FKP);
        dataset.setValue("ФИТУ", FITU);
        dataset.setValue("ФКСИС", FKSIS);
        dataset.setValue("ФРЭ", FRE);
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
