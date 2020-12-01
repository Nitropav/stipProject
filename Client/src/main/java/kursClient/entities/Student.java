package kursClient.entities;

import java.io.Serializable;
import java.util.Objects;

public class Student extends Person implements Serializable {
    private int ID;
    private String fullName;
    private String studentGroup;
    private String specialType;
    private String marks;
    private int idSpeciality;
    private int idFaculty;
    private int idSpecialScolarship;
    private String facultyName;
    private String specialty;
    private float payment;
    private float totalPayment;

    public float getTotalPayment() { return totalPayment; }
    public void setTotalPayment(float totalPayment) { this.totalPayment = totalPayment; }

    public Student(int id, String fullName, String studentGroup, String specialType,
                   int idSpeciality, int idFaculty, int idSpecialScholarship) {
        this.ID = id;
        this.fullName = fullName;
        this.studentGroup = studentGroup;
        this.specialType = specialType;
        this.idSpeciality = idSpeciality;
        this.idFaculty = idFaculty;
        this.idSpecialScolarship = idSpecialScholarship;
    }

    public Student(String fullName, String studentGroup, String specialType) {
        this.fullName = fullName;
        this.studentGroup = studentGroup;
        this.specialType = specialType;
    }

    public Student(String fullName, String studentGroup) {
        this.fullName = fullName;
        this.studentGroup = studentGroup;
    }

    public Student() { }


    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public float getPayment() { return payment; }
    public void setPayment(float payment) { this.payment = payment; }
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }
    public String getSpecialType() { return this.specialType; }
    public void setSpecialType(String specialType) { this.specialType = specialType; }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getStudentGroup() {
        return studentGroup;
    }
    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }
    public String getMarks() { return marks; }
    public void setMarks(String marks) { this.marks = marks; }
    public int getIdSpeciality() { return idSpeciality; }
    public void setIdSpeciality(int idSpeciality) { this.idSpeciality = idSpeciality; }
    public int getIdFaculty() { return idFaculty; }
    public void setIdFaculty(int idFaculty) { this.idFaculty = idFaculty; }
    public int getIdSpecialScolarship() { return idSpecialScolarship; }
    public void setIdSpecialScolarship(int idSpecialScolarship) { this.idSpecialScolarship = idSpecialScolarship; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(fullName, student.fullName) &&
                Objects.equals(studentGroup, student.studentGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, studentGroup);
    }

    @Override
    public String toString() {
        return "Student{" +
                "fullName='" + fullName + '\'' +
                ", studentGroup='" + studentGroup + '\'' +
                ", specialType'" + specialType + '\'' +
                ", marks'" + marks + '\'' +
                ", idSpeciality'" + idSpeciality + '\'' +
                ", idFaculty'" + idFaculty + '\'' +
                ", idSpecialScolarship'" + idSpecialScolarship + '\'' +
                ", facultyName'" + facultyName + '\'' +
                ", specialty'" + specialty + '\'' +
                ", payment'" + payment + '\'' +
                '}';
    }
}
