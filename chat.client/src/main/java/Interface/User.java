/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interface;

/**
 *
 * @author THIETKE
 */
public class User {

    private String user;
    private String password;

    public User(String userString, String passString) {
        this.user = userString;
        this.password = passString;
    }

    public String getUser() {
        return user.toString();
    }

    public String getPassword() {
        return password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
