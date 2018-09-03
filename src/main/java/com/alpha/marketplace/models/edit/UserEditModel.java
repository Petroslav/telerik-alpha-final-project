package com.alpha.marketplace.models.edit;

import org.springframework.web.multipart.MultipartFile;

public class UserEditModel {

    private String firstName;
    private String lastName;
    private MultipartFile picture;
    private String oldPass;
    private String newPass;

    public UserEditModel(String firstName, String lastName, MultipartFile picture, String oldPass, String newPass) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.picture = picture;
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

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
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
