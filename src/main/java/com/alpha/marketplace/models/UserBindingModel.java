package com.alpha.marketplace.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserBindingModel {

    @NotNull
    @Email
    private String email;

    private String firstName;
    private String lastName;

    @NotNull
    private String pass1;
    @NotNull
    private String pass2;

    public UserBindingModel(){}

    public UserBindingModel(String email, String firstName, String lastName, String pass1, String pass2) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pass1 = pass1;
        this.pass2 = pass2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
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

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }
}
