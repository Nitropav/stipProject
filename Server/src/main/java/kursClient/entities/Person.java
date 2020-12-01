package kursClient.entities;

import java.io.Serializable;
import java.util.Objects;

public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1353141269246738673L;
    private String login;
    private String password;
    protected String language;
    protected String name;
    protected String surname;
    protected int age;
    public Person() {
    }

    public Person(String login, String password, String language,String name,String surname,int age) {
        this.name=name;
        this.surname=surname;
        this.age=age;
    }
    public Person(String login, String password, String language) {
        this.login = login;
        this.password = password;
        this.language=language;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person human = (Person) o;
        return age == human.age &&
                Objects.equals(name, human.name) &&
                Objects.equals(surname, human.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age);
    }

    @Override
    public String toString() {
        return "Human{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +

                '}';
    }
}





