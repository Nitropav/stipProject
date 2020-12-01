package kursClient.entities;

import java.io.Serializable;
import java.util.Objects;

public class Admin extends Person implements Serializable {
    private int ID;
    private String login;
    private String password;
    private String role;

    public Admin(int id, String login, String password, String role) {
        this.ID = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Admin(String login, String password, String role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Admin(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Admin() { }

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(login, admin.login) &&
                Objects.equals(password, admin.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role'" + role + '\'' +
                '}';
    }
}
