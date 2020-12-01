package kursClient.db;
import kursClient.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBWorker {
    private static String URL;
    private static String LOGIN;
    private static String PASSWORD;
    private static volatile DBWorker instance;
    private static Connection connection;

    public static DBWorker getInstance() {
        if (instance == null) {
            synchronized (DBWorker.class) {
                if (instance == null) {
                    instance = new DBWorker();
                    try {
                        loadDataBaseProperties();
                        connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    private static void loadDataBaseProperties() {
        DBConfigurator configurator = new DBConfigurator();

        DBProperties dbProperties = configurator.getProperties();

        URL = dbProperties.getUrl();
        LOGIN = dbProperties.getLogin();
        PASSWORD = dbProperties.getPassword();
    }

    public synchronized String isAuthorised(Admin admin) {
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {

                // check for admin
                ResultSet resultSetAdmin = statement.executeQuery("SELECT *\n" +
                        "FROM admin\n" +
                        "WHERE login like '" + admin.getLogin() + "' and password like '" + admin.getPassword() + "';");

                if (resultSetAdmin.next()) {
                    if(resultSetAdmin.getString("ROLE").equals("admin")){
                        return "Admin";
                    } else if(resultSetAdmin.getString("ROLE").equals("booker")){
                        return "Teacher";
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized List<Student> readStudents() {
        List<Student> students = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {

                ResultSet resultSet = statement.executeQuery("SELECT * FROM student INNER JOIN faculty ON student.ID_faculty = faculty.ID_faculty \n" +
                        "                INNER JOIN specialty ON student.ID_Specialty = specialty.ID_Specialty");

                while (resultSet.next()) {
                    Student student = new Student();
                    student.setID(resultSet.getInt("ID_Student"));
                    student.setFullName(resultSet.getString("FullName"));
                    student.setStudentGroup(resultSet.getString("StudentGroup"));
                    student.setSpecialType(resultSet.getString("specialType"));
                    student.setMarks(resultSet.getString("marks"));
                    student.setIdSpeciality(resultSet.getInt("ID_Specialty"));
                    student.setIdFaculty(resultSet.getInt("ID_faculty"));
                    student.setIdSpecialScolarship(resultSet.getInt("ID_SpecScolarship"));
                    student.setFacultyName(resultSet.getString("Name"));
                    student.setSpecialty(resultSet.getString("Specialty"));
                    student.setPayment(resultSet.getFloat("coefficient"));
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public synchronized String addUser(Admin admin) {
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {
                statement.executeUpdate("INSERT into admin (id, login, password, role)\n" +
                        "VALUES ('" + admin.getID() + "',\n" +
                        "        '" + admin.getLogin() + "', \n" +
                        "        '" + admin.getPassword() + "', \n" +
                        "        '" + admin.getRole() + "');");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized String addStudent(Student student) {
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {
                statement.executeUpdate("INSERT into student (ID_Student, FullName, StudentGroup, " +
                        "specialType, ID_Specialty, ID_faculty, ID_SpecScolarship)\n" +
                        "VALUES ('" + student.getID() + "',\n" +
                        "        '" + student.getFullName() + "', \n" +
                        "        '" + student.getStudentGroup() + "', \n" +
                        "        '" + student.getSpecialType() + "', \n" +
                        "        '" + student.getIdSpeciality() + "', \n" +
                        "        '" + student.getIdFaculty() + "', \n" +
                        "        '" + student.getIdSpecialScolarship() + "');");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized List<Admin> readUsers() {
        List<Admin> users = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {

                ResultSet resultSet = statement.executeQuery("SELECT * FROM admin;");

                while (resultSet.next()) {
                    Admin admin = new Admin();
                    admin.setID(resultSet.getInt("id"));
                    admin.setLogin(resultSet.getString("login"));
                    admin.setPassword(resultSet.getString("password"));
                    admin.setRole(resultSet.getString("role"));
                    users.add(admin);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    public synchronized List<Specialty> readSpecialties() {
        List<Specialty> specialties = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM specialty;");
                while (resultSet.next()) {
                    Specialty specialty = new Specialty();
                    specialty.setID(resultSet.getInt("ID_Specialty"));
                    specialty.setSpecialtyName(resultSet.getString("Specialty"));
                    specialties.add(specialty);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialties;
    }


    public synchronized List<Faculty> readFaculties() {
        List<Faculty> faculties = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM faculty;");
                while (resultSet.next()) {
                    Faculty faculty = new Faculty();
                    faculty.setID(resultSet.getInt("ID_faculty"));
                    faculty.setFacultyName(resultSet.getString("Name"));
                    faculties.add(faculty);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return faculties;
    }
    //UPDATE `student` SET `marks` = '98765' WHERE `ID_Student` = 2

    public synchronized String setMarks(Student student) {
        try (Statement statement = connection.createStatement()) {
            String query = "update student set marks = ? where ID_Student = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString   (1, student.getMarks());
            preparedStmt.setInt(2, student.getID());

            if (!connection.isClosed()) {
                preparedStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized String setStudent(Student student) {
        try (Statement statement = connection.createStatement()) {
            String query = "update student set FullName = ?, StudentGroup = ?, specialType=?, ID_Specialty=?, ID_faculty=?  where ID_Student = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString   (1, student.getFullName());
            preparedStmt.setString   (2, student.getStudentGroup());
            preparedStmt.setString   (3, student.getSpecialType());
            preparedStmt.setInt   (4, student.getIdSpeciality());
            preparedStmt.setInt   (5, student.getIdFaculty());
            preparedStmt.setInt(6, student.getID());

            if (!connection.isClosed()) {
                preparedStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized String setAdmin(Admin admin) {
        try (Statement statement = connection.createStatement()) {
            String query = "update admin set login = ?, password=?, role=?  where id = ?";
            PreparedStatement preparedStmt = connection.prepareStatement(query);

            preparedStmt.setString(1, admin.getLogin());
            preparedStmt.setString(2, admin.getPassword());
            preparedStmt.setString(3, admin.getRole());
            preparedStmt.setInt(4, admin.getID());;

            if (!connection.isClosed()) {
                preparedStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public synchronized String deleteUser(int id) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {
                statement.executeUpdate("DELETE from admin where id =" +id);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized String deleteStudent(int id) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {
                statement.executeUpdate("DELETE from student where ID_Student =" +id);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}
