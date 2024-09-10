package model;

public class UserEdit {
    private String newEmail;
    private String newPassword;
    private String newName;

    public UserEdit(String newEmail, String newPassword, String newName) {
        this.newEmail = newEmail;
        this.newPassword = newPassword;
        this.newName = newName;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}