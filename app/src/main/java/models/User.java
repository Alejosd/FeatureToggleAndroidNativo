package models;

public class User {

    private String username;
    private String numberPhone;

    public User(String username, String numberPhone){
        this.username=username;
        this.numberPhone = numberPhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }
}
