package kursClient.view.menu;
        import kursClient.entities.Student;
        import kursClient.view.Diagrams.StudentDiagramWindow;
//import kursClient.view.Report.StudentReportFrame;
        import kursClient.view.UserFrames.RateStudentsWindow;
        import kursClient.view.UserFrames.UserShowStudents;
        import kursClient.view.editUserFrames.MainEditFrame;

        import javax.swing.*;
        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.net.Socket;
        import java.util.List;

public class UserWindow extends JFrame {

    private JLabel menuAdminLabel;
    private JButton reportsButton;
    private JButton editButton;
    private JButton rateButton;
    private JButton showButton;
    private JButton diagramButton;
    private JButton backButton;
    private JPanel panel;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private List<Student> students;
//    private List<Sportsman> sportsmen;
//    private String currentRefereeLogin;
//    private Referee entryReferee;


    public UserWindow(JFrame parent, Socket socket,
                      ObjectOutputStream objectOutputStream,
                      ObjectInputStream objectInputStream,
                      String login) {
        super("Бухгалтер: меню");
        setSize(500, 450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.socket = socket;
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;


        parent.setVisible(false);

        init();

        editButton.addActionListener(event -> {
            MainEditFrame mainEditFrame =
                    new MainEditFrame(this, socket,
                            objectOutputStream, objectInputStream,
                            students);
            mainEditFrame.setVisible(true);
            mainEditFrame.setLocationRelativeTo(null);
        });

        showButton.addActionListener(event -> {
            UserShowStudents userShowStudents =
                    new UserShowStudents(this, socket,
                            objectOutputStream, objectInputStream,
                            students);
            userShowStudents.setVisible(true);
            userShowStudents.setLocationRelativeTo(null);
        });

        rateButton.addActionListener(event -> {
            RateStudentsWindow rateStudentsWindow =
                    new RateStudentsWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            students);
            rateStudentsWindow.setVisible(true);
            rateStudentsWindow.setLocationRelativeTo(null);
        });

/*        diagramButton.addActionListener(event -> {
            StudentDiagramWindow studentDiagramWindow =
                    new StudentDiagramWindow(this, socket,
                            objectOutputStream, objectInputStream,
                            students);
            studentDiagramWindow.setVisible(true);
            studentDiagramWindow.setLocationRelativeTo(null);
        });*/



        backButton.addActionListener(event -> {
            this.dispose();
            parent.setVisible(true);
        });

        loadAllStudentsFromServer();
    }

    private void loadAllStudentsFromServer() {
        try {
            objectOutputStream.writeObject("getAllStudents");
            objectOutputStream.writeObject(null);
            students = (List<Student>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuAdminLabel = new JLabel("Главное меню: ");
        menuAdminLabel.setLocation(200, 10);
        menuAdminLabel.setSize(140, 50);

        editButton = new JButton("Меню редактирования");
        editButton.setLocation(145, 70);
        editButton.setSize(210, 30);

        rateButton = new JButton("Выставить оценки");
        rateButton.setLocation(145, 120);
        rateButton.setSize(210, 30);

        showButton = new JButton("Просмотр");
        showButton.setLocation(145, 170);
        showButton.setSize(210, 30);

        /*diagramButton = new JButton("Статистика");
        diagramButton.setLocation(145, 220);
        diagramButton.setSize(210, 30);*/

        backButton = new JButton("Назад");
        backButton.setLocation(275, 320);
        backButton.setSize(80, 30);

        panel = new JPanel();
        panel.setLayout(null);

        panel.add(rateButton);
        panel.add(backButton);
        panel.add(editButton);

        panel.add(menuAdminLabel);
        panel.add(showButton);
        //  panel.add(diagramButton);

        add(panel);
    }
}
