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
    private String pass;

    public UserBindingModel(){}

    public UserBindingModel(
            @NotBlank @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters") String username,
            @NotEmpty @Email(message = "Please provide a valid e-mail")
            @Size(min = 5, max = 36, message = "Please input an e-mail between 5 and 36 characters.") String email,
            @Size(max = 30, message = "Cannot be more than 30 characters") String firstName,
            @Size(max = 30, message = "Cannot be more than 30 characters") String lastName,
            @NotEmpty @Size(min = 6, max = 16, message = "Password must be between 6 and 16 symbols") String pass
    ) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pass = pass;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
