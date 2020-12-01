package kursClient.entities;

import java.io.Serializable;

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

}




