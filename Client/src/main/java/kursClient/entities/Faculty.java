package kursClient.entities;

import java.io.Serializable;
import java.util.Objects;

public class Faculty extends Person implements Serializable {
    private int ID;
    private String facultyName;

    public Faculty(int id, String facultyName, String password, String role) {
        this.ID = id;
        this.facultyName = facultyName;
    }

    public Faculty() { }

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getFacultyName() {
        return facultyName;
    }
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(facultyName, faculty.facultyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facultyName);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id='" + ID + '\'' +
                ", facultyName='" + facultyName + '\'' + '}';
    }
}
