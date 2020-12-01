package kursClient.entities;

import java.io.Serializable;
import java.util.Objects;

public class Specialty extends Person implements Serializable {
    private int ID;
    private String specialtyName;

    public Specialty(int id, String specialtyName, String password, String role) {
        this.ID = id;
        this.specialtyName = specialtyName;
    }

    public Specialty() { }

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public String getSpecialtyName() {
        return specialtyName;
    }
    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Specialty specialty = (Specialty) o;
        return Objects.equals(specialtyName, specialty.specialtyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialtyName);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id='" + ID + '\'' +
                ", specialtyName='" + specialtyName + '\'' + '}';
    }
}
