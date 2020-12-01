//package kursClient.view.Report;
//
//import kursClient.entities.Student;
//
//import javax.swing.*;
//import java.awt.*;
//import java.io.File;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//public class StudentReportFrame extends JFrame {
//    private static final String FILE_NAME_PATTERN = "[ a-zA-Z\\d]+";
//    private JLabel menuAdminLabel;
//
//    private JTextField fileNameField;
//    private JButton backButton;
//    private JButton createReportButton;
//    private JPanel panel;
//    private String report;
//
//
//    private Socket socket;
//    private ObjectOutputStream objectOutputStream;
//    private ObjectInputStream objectInputStream;
//
//    private List<Student> students;
//    private String currentRefereeLogin;
//
//
//
//    public StudentReportFrame(JFrame parent, Socket socket,
//                              ObjectOutputStream objectOutputStream,
//                              ObjectInputStream objectInputStream,
//                              List<Student> students) {
//        super("Бухгалтер: отчёт");
//        setSize(320, 380);
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//        this.socket = socket;
//        this.objectInputStream = objectInputStream;
//        this.objectOutputStream = objectOutputStream;
//
//        this.students = students;
////        this.teachers = teachers;
//
//        parent.setVisible(false);
//
//        init();
//
//        createReportButton.addActionListener(e -> {
//            String fileName = fileNameField.getText();
//
//            if (fileName.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Введите название файла");
//                return;
//            }
//
//            if (!isValid(fileName)) {
//                JOptionPane.showMessageDialog(this,
//                        "В названии фала должны быть только цифры и латинские буквы");
//                return;
//            }
//
//            fileName = "aaa" + " - " + fileName;
//
//            if (isExist(fileName)) {
//                JOptionPane.showMessageDialog(this,
//                        "Файл с таким названием уже существует");
//                return;
//            }
//
//            report = createReport();
//
//            try {
//
//                if (!Files.exists(Paths.get("C:\\321" +"aaa"))) {
//                    Files.createDirectory(Paths.get("C:\\321" +
//                            "aaa"));
//                }
//                Files.write(Paths.get("C:\\321" +
//                        "aaa" + "/" + fileName + ".txt"), report.getBytes());
//            } catch (IOException e1) {
//                JOptionPane.showMessageDialog(this,
//                        "Не удалось создать отчёт");
//                return;
//            }
//
//            File file = new File("C:\\321" +
//                    "aaa" + "/" + fileName + ".txt");
//
//            try {
//                Desktop desktop = Desktop.getDesktop();
//                desktop.open(file);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//
//            fileNameField.setText("");
//
//        });
//
//        backButton.addActionListener(event -> {
//            this.dispose();
//            parent.setVisible(true);
//        });
//
//    }
//
//    private boolean isValid(String fileName) {
//        return fileName.matches(FILE_NAME_PATTERN);
//    }
//
//    private boolean isExist(String fileName) {
//        return Files.exists(Paths.get("C:\\321" +
//                "aaa" + "/" +
//                fileName + ".txt"));
//    }
//
//    private String createReport() {
//        StringBuilder textReport = new StringBuilder("\t\t\t" + "aaa" + "\r\n");
//        textReport.append("\t\t").append(entryTeacher.getName()).append(" ").append(entryTeacher.getSurname());
//        textReport.append(" (").append(entryTeacher.getLanguage()).append(")\r\n");
//        textReport.append("\tВы оценили следующих студентов:\r\n");
//        int counter = 1;
//        for (Student student : students) {
//
//            if (!student.getPerformance().getMarks().isEmpty() &&
//                    student.getPerformance().getMarks().containsKey(entryTeacher) &&
//                    student.getPerformance().getMarks().get(entryTeacher) != null) {
//
//                textReport.append(counter).
//                        append(") ").
//                        append(student.getName()).
//                        append(" ").
//                        append(student.getSurname());
//
//                Mark mark = student.getPerformance().getMarks().get(entryTeacher);
//                if (entryTeacher.getLanguage().equals("Английский")) {
//                    textReport.append(" - За аудирование: ").append(((EnglishMark) mark).getTechnicalMark());
//                    textReport.append(", за говорение: ").append(((EnglishMark) mark).getPresentationMark());
//                    textReport.append(".\r\n");
//                }
//                if (entryTeacher.getLanguage().equals("Китайский")) {
//                    textReport.append(" - За аудирование: ").append(((ChineseMark) mark).getTechnicalMark());
//                    textReport.append(", за говорение: ").append(((ChineseMark) mark).getPresentationMark());
//                    textReport.append(".\r\n");
//                }
//                if (entryTeacher.getLanguage().equals("Немецкий")) {
//                    textReport.append(" - За аудирование: ").append(((GermanMark) mark).getTechnicalMark());
//                    textReport.append(", за говорение: ").append(((GermanMark) mark).getPresentationMark());
//                    textReport.append(".\r\n");
//                }
//                counter++;
//            }
//        }
//        if (counter == 1) {
//            textReport.append("Пока вы оценили ни одного экзамена.");
//        }
//        return textReport.toString();
//    }
//
//    private void init() {
//        JMenuBar menuBar = new JMenuBar();
//        setJMenuBar(menuBar);
//
//        JMenu menu = new JMenu("Сохранить");
//        menuBar.add(menu);
//
//
//        menuAdminLabel = new JLabel("Укажите название файла: ");
//        menuAdminLabel.setLocation(80, 10);
//        menuAdminLabel.setSize(160, 50);
//
//        fileNameField = new JTextField();
//        fileNameField.setLocation(80, 70);
//        fileNameField.setSize(150, 30);
//
//        backButton = new JButton("Назад");
//        backButton.setLocation(10, 270);
//        backButton.setSize(80, 30);
//
//
//        createReportButton = new JButton("Создать отчёт");
//        createReportButton.setLocation(170, 270);
//        createReportButton.setSize(120, 30);
//
//        panel = new JPanel();
//        panel.setLayout(null);
//
//        panel.add(fileNameField);
//        panel.add(backButton);
//        panel.add(createReportButton);
//        panel.add(menuAdminLabel);
//
//        add(panel);
//    }
//}
//
