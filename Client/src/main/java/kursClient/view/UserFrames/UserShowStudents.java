package kursClient.view.UserFrames;


import kursClient.entities.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.print.PrinterException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.List;

public class UserShowStudents extends JFrame {
    private JPanel panel;
    private JButton backButton;
    private JButton sortByMarksButton;
    private JButton makeReport;

    private JButton sortByScolarshipButton;
    private JLabel sortLabel;

    private DefaultTableModel dtm;

    private JTable table;
    private JTable newTable;
    JScrollPane scrollPane;
    JScrollPane scrollPane1;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;

    private List<Student> sortedStudents;

    public UserShowStudents(JFrame parent, Socket socket,
                                ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,
                                List<Student> students) {

        super("Students");
        setSize(830, 460);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;

        this.students = students;

        parent.setVisible(false);

        init();

        sortByMarksButton.addActionListener(event -> {
            sortedStudents = students;
            Student buf;
            boolean isSorted = false;
            while(!isSorted){
                isSorted = true;
                for(int i = 0; i < sortedStudents.size() - 1; i++){
                    if(countMarks(sortedStudents.get(i).getMarks()) < countMarks(sortedStudents.get(i+1).getMarks())){
                        isSorted = false;
                        buf = sortedStudents.get(i);
                        sortedStudents.set(i, sortedStudents.get(i+1)) ;
                        sortedStudents.set(i+1, buf);
                    }
                }
            }
            for (int i = dtm.getRowCount() - 1; i >= 0; i--) {
                dtm.removeRow(i);
            }
            for(int i = 0; i < sortedStudents.size();i++){
                int j = i + 1;
                dtm.addRow(new String[]{String.valueOf(j),
                        sortedStudents.get(i).getFullName(),
                        sortedStudents.get(i).getStudentGroup(),
                        sortedStudents.get(i).getSpecialType(),
                        parseMarks(sortedStudents.get(i).getMarks()),
                                sortedStudents.get(i).getSpecialty(),
                                sortedStudents.get(i).getFacultyName(),
                        countScholarship(sortedStudents.get(i))});
            }
        });

        sortByScolarshipButton.addActionListener(event -> {
            sortedStudents = students;
            Student buf;
            boolean isSorted = false;
            while(!isSorted){
                isSorted = true;
                for(int i = 0; i < sortedStudents.size() - 1; i++){
                    if(sortedStudents.get(i).getTotalPayment() < sortedStudents.get(i+1).getTotalPayment()){
                        isSorted = false;
                        buf = sortedStudents.get(i);
                        sortedStudents.set(i, sortedStudents.get(i+1)) ;
                        sortedStudents.set(i+1, buf);
                    }
                }
            }
            for (int i = dtm.getRowCount() - 1; i >= 0; i--) {
                dtm.removeRow(i);
            }
            for(int i = 0; i < sortedStudents.size();i++){
                int j = i + 1;
                dtm.addRow(new String[]{String.valueOf(j),
                        sortedStudents.get(i).getFullName(),
                        sortedStudents.get(i).getStudentGroup(),
                        sortedStudents.get(i).getSpecialType(),
                        parseMarks(sortedStudents.get(i).getMarks()),
                        sortedStudents.get(i).getSpecialty(),
                        sortedStudents.get(i).getFacultyName(),
                        countScholarship(sortedStudents.get(i))});
            }
        });


        makeReport.addActionListener(event -> {
            MessageFormat header = new MessageFormat("Отчет");
            MessageFormat footer = new MessageFormat("Конец отчета");
            try{
                table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
            } catch(PrinterException e) {
                JOptionPane.showMessageDialog(null, "Ошибка создания отчета");
            }
        });

        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

    }

    private Float countMarks(String str){
        if(str == null){
            return Float.valueOf(0);
        }
        char[] strToArray = str.toCharArray();
        float sum = 0;
        for(int i = 0; i < strToArray.length; i++) {
            sum += Character.getNumericValue(strToArray[i]);
        }
        sum = sum / strToArray.length;
        return sum;
    }

    private void init() {
        panel = new JPanel();
        panel.setLayout(null);

        backButton = new JButton("Назад");
        backButton.setLocation(10, 380);
        backButton.setSize(80, 30);

        sortLabel = new JLabel("Сортировать ");
        sortLabel.setLocation(635, 330);
        sortLabel.setSize(80, 50);

        sortByMarksButton = new JButton("По оценкам");
        sortByMarksButton.setLocation(680, 380);
        sortByMarksButton.setSize(120, 30);

        sortByScolarshipButton = new JButton("По стипендии");
        sortByScolarshipButton.setLocation(530, 380);
        sortByScolarshipButton.setSize(120, 30);

        makeReport = new JButton("Создать отчет");
        makeReport.setLocation(380, 380);
        makeReport.setSize(120, 30);

        JLabel title = new JLabel("Студенты");
        title.setLocation(355, 10);
        title.setSize(120, 30);

        dtm = new DefaultTableModel(getData(), getHeaders());
        table = new JTable(dtm);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setLocation(10, 50);
        scrollPane.setSize(790, 270);

        panel.add(backButton);
        panel.add(sortByMarksButton);
        panel.add(sortByScolarshipButton);
        panel.add(sortLabel);
        panel.add(makeReport);
        panel.add(scrollPane);
        panel.add(title);

        add(panel);
    }

    private String[][] getData() {
        String[][] strings = new String[students.size()][8];
        for (int i = 0; i < strings.length; i++) {
            int j = i + 1;
            strings[i][0] = String.valueOf(j);
            strings[i][1] = students.get(i).getFullName();
            strings[i][2] = students.get(i).getStudentGroup();
            strings[i][3] = students.get(i).getSpecialType();
            strings[i][4] = parseMarks(students.get(i).getMarks());
            strings[i][5] = students.get(i).getSpecialty();
            strings[i][6] = students.get(i).getFacultyName();
            strings[i][7] = countScholarship(students.get(i));
        }

        return strings;
    }

    private String[][] getNewData() {
        String[][] strings = new String[sortedStudents.size()][8];
        for (int i = 0; i < strings.length; i++) {
            int j = i + 1;
            strings[i][0] = String.valueOf(j);
            strings[i][1] = sortedStudents.get(i).getFullName();
            strings[i][2] = sortedStudents.get(i).getStudentGroup();
            strings[i][3] = sortedStudents.get(i).getSpecialType();
            strings[i][4] = parseMarks(sortedStudents.get(i).getMarks());
            strings[i][5] = sortedStudents.get(i).getSpecialty();
            strings[i][6] = sortedStudents.get(i).getFacultyName();
            strings[i][7] = countScholarship(sortedStudents.get(i));
        }

        return strings;
    }


    private String countScholarship(Student student){
        if(student.getMarks() == null){
            return "no marks";
        }
        char[] strToArray = student.getMarks().toCharArray();
        float sum = 0;
        for(int i = 0; i < strToArray.length; i++) {
            sum += Character.getNumericValue(strToArray[i]);
        }
        sum = sum / strToArray.length;
        float scholarship = (float) (77.08*getCoef(sum)+specScholarship(student.getSpecialType()));
        double d = new BigDecimal(scholarship).setScale(2, RoundingMode.UP).doubleValue();
        student.setPayment((float) d);
        student.setTotalPayment((float) d);
        return String.valueOf(d);
    }


    private float specScholarship(String type){
        switch(type){
            case "social": return (float) 61.09;
            case "academic": return (float) 134.89;
            case "nominal": return (float) 177.94;
            default: return 0;
        }
    }

    private float getCoef(float sum){
        if(sum < 6){
            return 1;
        } else if(sum < 8) {
            return (float) 1.2;
        } else if(sum < 9) {
            return (float) 1.4;
        } else if(sum < 10){
            return (float) 1.6;
        } else return 1;
    }

    private String[] getHeaders() {
        String[] strings = {"#", "Full name", "Group", "Special scholarship ", "Marks", "Specialty", "Faculty", "Total Scholarship"};
        return strings;
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
