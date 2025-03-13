package ca.uoguelph.backend;

public class User {
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
    }

    public boolean login(String id, String password) {
        return (this.id == id && this.password == password);
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
