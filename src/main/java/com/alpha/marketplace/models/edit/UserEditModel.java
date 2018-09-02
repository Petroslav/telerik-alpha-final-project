package com.alpha.marketplace.models.edit;

public class UserEditModel {

    private String firstName;
    private String lastName;
    private String oldPass;
    private String newPass;

    public UserEditModel(String firstName, String lastName, String oldPass, String newPass) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.oldPass = oldPass;
        this.newPass = newPass;
    }

    public String getFirstName() {   return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}
