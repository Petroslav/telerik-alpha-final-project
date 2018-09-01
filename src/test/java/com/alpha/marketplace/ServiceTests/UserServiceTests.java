package com.alpha.marketplace.ServiceTests;

import com.alpha.marketplace.models.Role;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.repositories.base.CloudUserRepository;
import com.alpha.marketplace.repositories.base.RoleRepository;
import com.alpha.marketplace.repositories.base.UserRepository;
import com.alpha.marketplace.services.UserServiceImpl;
import com.alpha.marketplace.services.base.UserService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudUserRepository cloudUserRepository;

    @Mock
    private BindingResult errors;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    private final String EMAIL_EXISTS = "troqta@gmail.com";
    private final String EMAIL_AVAILABLE = "notpresent@present.present";

    private UserService userService;
    private User expected = null;

    @Before
    public void setup(){
        expected = UserSetUp.setupUser("user","FirstoNaimo", "LastoNaimo", EMAIL_EXISTS, "1234567");
        when(userRepository.save(any(User.class)))
                .thenReturn(true);

        when(userRepository.findByEmail(EMAIL_EXISTS))
                .thenReturn(expected);
        when(userRepository.findByEmail(EMAIL_AVAILABLE))
                .thenReturn(null);
        when(encoder.encode(anyString()))
                .thenReturn("encryptedPass");
        when(mapper.map(any(UserBindingModel.class), eq(User.class)))
                .thenReturn(expected);
        when(roleRepository.findByName(anyString()))
                .thenReturn(new Role("ROLE_USER"));

        userService = new UserServiceImpl(userRepository, cloudUserRepository, roleRepository, mapper, encoder);
    }

    @Test
    public void userServiceRegistrationSuccess() {
        UserBindingModel reg = new UserBindingModel("user", EMAIL_AVAILABLE, "FirstoNaimo", "LastoNaimo", "1234567" );
        User success = userService.registerUser(reg, errors);
        Assert.assertThat(success, samePropertyValuesAs(expected));
    }

    @Test
    public void userServiceRegistrationFail(){
        UserBindingModel reg = new UserBindingModel("user", EMAIL_EXISTS, "FirstoNaimo", "LastoNaimo", "1234567" );

        User success = userService.registerUser(reg, errors);
        Assert.assertNull(success);

    }
}
