package kursClient.view;

import kursClient.server.ServerConnection;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerWindow extends JFrame{
    private static final int ACTIVE_PORT = 8080;
    private InetAddress addr = InetAddress.getByName("127.0.0.1");
    private int backlog = 50;
    private int port = 8080;;
    private int count = 0;


    private JPanel panel;
    private JButton startButton;
    private JButton endButton;
    private JButton loadToReserv;
    private JButton getFromReserv;
    private JLabel socketStatus;
    private JLabel activePort, connectionLabel, serverLabel, fileLabel;
    private JTextArea connectedList;

    private boolean serverWork;
    private ExecutorService executorService;
    private ServerSocket serverSocket;

    public ServerWindow() throws HeadlessException, UnknownHostException {
        super("Server");
        setSize(600, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        init();

        Runnable startServer = () -> {
            try {
//                setServerParameters();
                serverSocket = new ServerSocket(port, backlog, addr);
                while (serverWork) {
                    System.out.println("wait for connection ...");

                    Socket sock = serverSocket.accept();
                    count++;
                    System.out.println(sock.getInetAddress().getHostName() + " connected");
                    connectedList.append(count + " - Adress: " + sock.getInetAddress() +
                            " port: " + sock.getPort() + " date: " + (new Date()).toString() + "\n");

                    ServerConnection server = new ServerConnection(sock);
                    server.start();//запуск потока
                }
            } catch (SocketException e) {
                socketStatus.setText("Сервер остановлен");
                System.out.println("closed");
            } catch (IOException e) {
                e.getStackTrace();
            }
        };

        startButton.addActionListener(event -> {
            if (!serverWork) {
                executorService = Executors.newSingleThreadExecutor();
                serverWork = true;

                executorService.execute(startServer);
                socketStatus.setText("Сервер запущен");
            }
        });

        endButton.addActionListener(event -> {
            if (serverWork) {
                serverWork = false;
                count = 0;
                executorService.shutdown();
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        loadToReserv.addActionListener(e -> {
//            DataBaseWorker dataBaseWorker = DataBaseWorker.getInstance();
//            java.util.List<Referee> referees = dataBaseWorker.readReferees();
//            java.util.List<Sportsman> sportsmen = dataBaseWorker.readSportsmen(referees);
//
//            DataBaseData dataBaseData = new DataBaseData(sportsmen, referees);
//
//            FileReaderWriter fileWriter = new FileReaderWriter();
//
//            fileWriter.setDbData(dataBaseData);
//            JOptionPane.showMessageDialog(this,"Сохранение данных из БД выполнено");
        });

        getFromReserv.addActionListener(e -> {
//            DataBaseData dataBaseData;
//
//            FileReaderWriter fileWriter = new FileReaderWriter();
//
//            dataBaseData = fileWriter.getDbData();
//
//            if (dataBaseData == null) {
//                JOptionPane.showMessageDialog(this, "Ошибка прочтения бэкап файла");
//                return;
//            }
//
//            java.util.List<Referee> referees = dataBaseData.getReferees();
//            List<Sportsman> sportsmen = dataBaseData.getSportsmen();
//
//            DataBaseWorker dataBaseWorker = DataBaseWorker.getInstance();
//            dataBaseWorker.addSportsmenAndReferees(sportsmen, referees);
//
//            JOptionPane.showMessageDialog(this,"Загрузка ранее сохранённых данных в БД выполнена");
        });
    }

    private void init() {
        panel = new JPanel();
        panel.setLayout(null);

        startButton = new JButton("Запустить");
        startButton.setLocation(220, 55);
        startButton.setSize(150, 30);

        endButton = new JButton("Остановить");
        endButton.setLocation(220, 90);
        endButton.setSize(150, 30);

        socketStatus = new JLabel("Сервер остановлен");
        socketStatus.setFont(new Font("Serif", Font.BOLD, 20));
        socketStatus.setLocation(70, 450);
        socketStatus.setSize(300, 100);
        socketStatus.setBackground(Color.pink);

        activePort = new JLabel("Стартовый порт: " + ACTIVE_PORT);
        activePort.setLocation(425, 450);
        activePort.setSize(200, 100);
        activePort.setBackground(Color.pink);

        loadToReserv = new JButton("Сохранить");
        loadToReserv.setLocation(350, 55);
        loadToReserv.setSize(100, 30);

        getFromReserv = new JButton("Нагрузка");
        getFromReserv.setLocation(350, 90);
        getFromReserv.setSize(100, 30);

        connectionLabel = new JLabel("Подключение");
        connectionLabel.setFont(new Font("Serif", Font.BOLD, 22));
        connectionLabel.setLocation(230, 100);
        connectionLabel.setSize(200, 100);
        connectionLabel.setBackground(Color.pink);



        serverLabel = new JLabel("Сервер");
        serverLabel.setFont(new Font("Serif", Font.BOLD, 20));
        serverLabel.setLocation(260, 25);
        serverLabel.setSize(170, 30);
        serverLabel.setBackground(Color.pink);


        connectedList = new JTextArea();
        connectedList.setLocation(50, 170);
        connectedList.setSize(500, 300);
        connectedList.setEditable(false);

        panel.add(startButton);
        panel.add(endButton);
        panel.add(socketStatus);
        panel.add(activePort);
        panel.add(connectedList);
//        panel.add(loadToReserv);
//        panel.add(getFromReserv);
        panel.add(connectionLabel);
        panel.add(serverLabel);

        add(panel);
    }
}

