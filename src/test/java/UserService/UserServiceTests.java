package UserService;

import com.alpha.marketplace.models.Role;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.models.edit.UserEditModel;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudUserRepository cloudUserRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private BCryptPasswordEncoder encoder;

    private final String EMAIL_EXISTS = "troqta@gmail.com";
    private final String EMAIL_AVAILABLE = "notpresent@present.present";
    private final String USERNAME_EXISTS = "exists";
    private final String USERNAME_AVAILABLE = "available";
    private final String PASS_OLD = "1234567";
    private final String PASS_NEW = "1111111";

    private UserService userService;
    private User expected = null;

    @Before
    public void setup(){
        expected = UserSetUp.setupUser(USERNAME_EXISTS,"FirstoNaimo", "LastoNaimo", EMAIL_EXISTS, PASS_OLD);

        when(userRepository.save(any(User.class)))
                .thenReturn(true);
        when(userRepository.findByEmail(EMAIL_EXISTS))
                .thenReturn(expected);
        when(userRepository.update(any(User.class)))
                .thenReturn(true);
        when(userRepository.findByEmail(EMAIL_AVAILABLE))
                .thenReturn(null);
        when(userRepository.findByUsername(USERNAME_EXISTS))
                .thenReturn(expected);
        when(userRepository.findByUsername(USERNAME_AVAILABLE))
                .thenThrow(new UsernameNotFoundException("Kek"));
        when(encoder.encode(PASS_OLD))
                .thenReturn("encryptedOldPass");
        when(encoder.encode(PASS_NEW))
                .thenReturn("encryptedNewPass");
        when(encoder.matches(PASS_OLD, PASS_OLD))
                .thenReturn(true);
        when(mapper.map(any(UserBindingModel.class), eq(User.class)))
                .thenReturn(expected);
        when(userRepository.findById(1))
                .thenReturn(expected);
        when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(new Role("ROLE_USER"));
        when(roleRepository.findByName("ROLE_ADMIN"))
                .thenReturn(new Role("ROLE_ADMIN"));

        userService = new UserServiceImpl(userRepository, cloudUserRepository, roleRepository, mapper, encoder);
    }

    @Test
    public void userServiceRegistrationSuccessByEmail() {
        UserBindingModel reg = new UserBindingModel(USERNAME_AVAILABLE, EMAIL_AVAILABLE, "FirstoNaimo", "LastoNaimo", PASS_OLD);
        User success = userService.registerUser(reg);
        Assert.assertThat(success, samePropertyValuesAs(expected));
    }

    @Test
    public void userServiceRegistrationFailByEmail(){
        UserBindingModel reg = new UserBindingModel("user", EMAIL_EXISTS, "FirstoNaimo", "LastoNaimo", PASS_OLD );

        User success = userService.registerUser(reg);
        Assert.assertNull(success);

    }

    @Test
    public void userServiceRegistrationFailPassword(){
        UserBindingModel reg = new UserBindingModel("user", EMAIL_AVAILABLE, "FirstoNaimo", "LastoNaimo", "123" );
        User success = userService.registerUser(reg);
        Assert.assertNull(success);
    }


    @Test
    public void userServiceRegistrationSuccessPassword(){
        UserBindingModel reg = new UserBindingModel(USERNAME_AVAILABLE, EMAIL_AVAILABLE, "FirstoNaimo", "LastoNaimo", PASS_OLD);
        User success = userService.registerUser(reg);
        Assert.assertNotNull(success);
    }

    @Test
    public void userServiceRegisterSuccessUsername(){
        UserBindingModel reg = new UserBindingModel(USERNAME_AVAILABLE, EMAIL_AVAILABLE, "FirstoNaimo", "LastoNaimo", PASS_OLD);
        User success = userService.registerUser(reg);
        Assert.assertNotNull(success);

    }

    @Test
    public void userServiceRegisterFAILUsername(){
        UserBindingModel reg = new UserBindingModel(USERNAME_EXISTS, EMAIL_AVAILABLE, "FirstoNaimo", "LastoNaimo", PASS_OLD);
        User success = userService.registerUser(reg);
        Assert.assertNull(success);

    }

    @Test
    public void userServiceEditUserOldPasswordMatchSuccess(){
        UserEditModel sad = new UserEditModel("ekler", "lastEkler", PASS_OLD, PASS_NEW);
        boolean kek = userService.editUser(expected, sad);
        Assert.assertTrue(kek);
    }

    @Test
    public void userServiceRemoveRoleFromUserSuccess(){
        userService.removeRoleFromUser(1, "USER");
        Assert.assertFalse(expected.getAuthorities().contains(new Role("ROLE_USER")));

    }

    @Test
    public void userServiceRemoveRoleFromUserFail(){
        userService.removeRoleFromUser(1, "ADMIN");
        Assert.assertTrue(expected.getAuthorities().contains(new Role("ROLE_USER")));
    }

    @Test
    public void userServiceAddRoleToUser(){
        Assert.assertFalse(expected.getAuthorities().contains(new Role("ROLE_ADMIN")));
        userService.addRoleToUser(1, "ADMIN");
        Assert.assertTrue(expected.getAuthorities().contains(new Role("ROLE_ADMIN")));
    }

    //TODO MAYBE TEST PIC METHODS EVEN THOUGH THEY ARE CLOUD BASED
}
