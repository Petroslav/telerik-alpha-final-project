package com.alpha.marketplace.models.binding;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserBindingModel {

    @NotBlank
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;

    @NotEmpty
    @Email(message = "Please provide a valid e-mail")
    @Size(min = 5, max = 36, message = "Please input an e-mail between 5 and 36 characters.")
    private String email;

    @Size(max = 30, message = "Cannot be more than 30 characters")
    private String firstName;

    @Size(max = 30, message = "Cannot be more than 30 characters")
    private String lastName;

    @NotEmpty
    @Size(min = 6, max = 16, message = "Password must be between 6 and 16 symbols")
    private String pass1;
    @NotEmpty
    @Size(min = 6, max = 16, message = "Password must be between 6 and 16 symbols")
    private String pass2;

    public UserBindingModel(){}

    public UserBindingModel(String username, String email, String firstName, String lastName, String pass1, String pass2) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pass1 = pass1;
        this.pass2 = pass2;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
