package Service;

import com.alpha.marketplace.models.User;

class UserSetUp {

    static User setupUser(String username, String firstName, String lastName, String email, String password) {
        User profile = new User();
        profile.setUsername(username);
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setEmail(email);
        profile.setPassword(password);

        return profile;
    }
}
