package kursClient.server;


import kursClient.db.DBWorker;
import kursClient.entities.Admin;
import kursClient.entities.Faculty;
import kursClient.entities.Specialty;
import kursClient.entities.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerConnection extends Thread {
    private Socket incoming;
    private String methodToCall;
    private DBWorker dbWorker;

    public ServerConnection(Socket incomingSocket) {
        this.incoming = incomingSocket;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(incoming.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(incoming.getOutputStream())) {

            while (!incoming.isClosed()) {
                String whatToDo = (String) objectInputStream.readObject();
                System.out.println("User choose: " + whatToDo);
                TimeUnit.MILLISECONDS.sleep(50);
                Object object = objectInputStream.readObject();
                chooseAction(whatToDo, object, objectOutputStream, objectInputStream);
                TimeUnit.MILLISECONDS.sleep(50);


            }
        } catch (SocketException e) {
            System.out.println("Клиент отсоединился");
        } catch (ClassNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Disconnect");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Disconnect");
        }
    }


    private void chooseAction(String nextAction, Object object, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream)
            throws IOException, SQLException {
        switch (nextAction) {
            case "authorisation":
                String answer = isAuthorised(object);
                if (answer != null) {
                    objectOutputStream.writeObject(answer);
                } else {
                    objectOutputStream.writeObject("false");
                }
                break;
            case "addUser":

                objectOutputStream.flush();
                objectOutputStream.reset();//

                Object  user = object;
                System.out.println("get object " + user);
                addUser(user);

                objectOutputStream.flush();
                objectOutputStream.reset();

                break;
            case "getAllUsers":
                List<Admin> users = readUsersFromDb();
                objectOutputStream.flush();
                objectOutputStream.reset();

                objectOutputStream.writeObject(users);

                objectOutputStream.flush();
                objectOutputStream.reset();

                break;

            case "getAllStudents":
                List<Student> students = readStudentsFromDb();
                objectOutputStream.flush();
                objectOutputStream.reset();

                objectOutputStream.writeObject(students);

                objectOutputStream.flush();
                objectOutputStream.reset();

                break;

            case "deleteUser":

                objectOutputStream.flush();
                objectOutputStream.reset();//

                Object deletedUser = object;
                deleteUser(deletedUser);

                objectOutputStream.flush();
                objectOutputStream.reset();

                break;

            case "deleteStudent":

                objectOutputStream.flush();
                objectOutputStream.reset();//

                deleteStudent(object);

                objectOutputStream.flush();
                objectOutputStream.reset();

                break;

            case "setMarks":
                objectOutputStream.flush();
                 objectOutputStream.reset();//

                setMarks(object);

                objectOutputStream.flush();
                objectOutputStream.reset();
                break;

            case "setStudent":
                objectOutputStream.flush();
                 objectOutputStream.reset();//

                setStudent(object);

                objectOutputStream.flush();
                objectOutputStream.reset();
                break;

            case "setAdmin":
                objectOutputStream.flush();
                objectOutputStream.reset();//

                setAdmin(object);

                objectOutputStream.flush();
                objectOutputStream.reset();
                break;
            case "getSpecialties":
                List<Specialty> specialties = readSpecialtiesFromDb();
                objectOutputStream.flush();
                objectOutputStream.reset();

                objectOutputStream.writeObject(specialties);

                objectOutputStream.flush();
                objectOutputStream.reset();

                break;

            case "getFaculties":
                List<Faculty> faculties = readFacultiesFromDb();
                objectOutputStream.flush();
                objectOutputStream.reset();

                objectOutputStream.writeObject(faculties);

                objectOutputStream.flush();
                objectOutputStream.reset();

                break;


            case "addStudent":

                objectOutputStream.flush();
                objectOutputStream.reset();//


                System.out.println("get object " + object);
                addStudent(object);

                objectOutputStream.flush();
                objectOutputStream.reset();

                break;
            case "setAll":
//                try {
//                    objectOutputStream.flush();
//                    List<Sportsman> sportsmenForAdd = (List<Sportsman>) objectInputStream.readObject();
//
//                    List<Referee> refereesForAdd = (List<Referee>) objectInputStream.readObject();
//
//                    objectOutputStream.flush();
//                    objectOutputStream.reset();
//                    objectOutputStream.writeObject(addSportsmenAndRefereesInBd(sportsmenForAdd, refereesForAdd));
//
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                break;
            case "calculate Result":
//                Sportsman sportsman = (Sportsman) object;
//
//                MarkCalculator calculator = new MarkCalculator();
//
//                double totalMark = calculator.calculate(sportsman);
//
//                String   sMark = String.format("%(.2f", totalMark);
//
//                objectOutputStream.flush();
//                objectOutputStream.reset();
//                objectOutputStream.writeObject(sMark);
//                break;
            default:
                throw new UnsupportedEncodingException("Unknown: " + nextAction);
        }
    }

    private List<Student> readStudentsFromDb() {
        dbWorker = dbWorker.getInstance();
        return dbWorker.readStudents();
    }

    private String isAuthorised(Object object) {
        Admin admin = (Admin) object;
        dbWorker = dbWorker.getInstance();
        return dbWorker.isAuthorised(admin);
    }

    private List<Admin> readUsersFromDb() {
        dbWorker = dbWorker.getInstance();
        return dbWorker.readUsers();
    }

    private String addUser(Object object) {
        Admin admin = (Admin) object;
        dbWorker = dbWorker.getInstance();
        return dbWorker.addUser(admin);
    }

    private String addStudent(Object object) {
        Student student = (Student) object;
        dbWorker = dbWorker.getInstance();
        return dbWorker.addStudent(student);
    }


    private String setStudent(Object object) {
        Student student = (Student) object;
        dbWorker = dbWorker.getInstance();
        return dbWorker.setStudent(student);
    }


    private String deleteUser(Object object) throws SQLException {
        Admin admin = (Admin) object;
        dbWorker = dbWorker.getInstance();
        return dbWorker.deleteUser(admin.getID());
    }

    private String deleteStudent(Object object) throws SQLException {
        Student student = (Student) object;
        dbWorker = dbWorker.getInstance();
        return dbWorker.deleteStudent(student.getID());
    }

    private String setMarks(Object object) throws SQLException {
        Student student = (Student) object;
        dbWorker = dbWorker.getInstance();
        return dbWorker.setMarks(student);
    }

    private List<Specialty> readSpecialtiesFromDb() {
        dbWorker = dbWorker.getInstance();
        return dbWorker.readSpecialties();
    }

    private List<Faculty> readFacultiesFromDb() {
        dbWorker = dbWorker.getInstance();
        return dbWorker.readFaculties();
    }
    private String setAdmin(Object object) {
        Admin admin = (Admin) object;
        dbWorker = dbWorker.getInstance();
        return dbWorker.setAdmin(admin);
    }

//
//    private List<Sportsman> readSportsmanFromBd(List<Referee> referees) {
//        dbWorker = dbWorker.getInstance();
//        return dbWorker.readSportsmen(referees);
//    }
//
//    private String addSportsmenAndRefereesInBd(List<Sportsman> sportsmen, List<Referee> referees) {
//        dbWorker = dbWorker.getInstance();
//        return dbWorker.addSportsmenAndReferees(sportsmen, referees);
//    }
}