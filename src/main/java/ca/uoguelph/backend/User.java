package ca.uoguelph.backend;

import ca.uoguelph.backend.login.LoginManager;

public class User extends RowData {
    private String id;
    private String password;
    private String email;
    private String profilePhoto;
    private String name;

    public User(String id, String password, String email, String name, String profilePhoto) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.name = name;
        this.profilePhoto = profilePhoto;
        LoginManager.addUser(this);
    }

    public boolean login(String id, String password) {
        return (this.id == id && this.password == password);
    }

    public String getID() {
        return id;
    }

    protected void setID(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    protected void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }
}
