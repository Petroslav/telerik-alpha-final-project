package ExtensionService;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.GitHubInfo;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.repositories.base.*;
import com.alpha.marketplace.services.ExtensionServiceImpl;
import com.alpha.marketplace.services.base.ExtensionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExtensionServiceBasicTests {

    @Mock
    private ExtensionRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private CloudExtensionRepository cloudExtensionRepository;

    @Mock
    private GitHubRepository gitHubRepository;

    @Mock
    private PropertiesRepository propertiesRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private BindingResult errors;

    private ExtensionService service;
    private Extension e;

    @Before
    public void init(){
        service = new ExtensionServiceImpl(repository, userRepository, cloudExtensionRepository, tagRepository, mapper, gitHubRepository, propertiesRepository);

        when(repository.getAll())
                .thenReturn(Arrays.asList(
                        ExtensionServiceSetUp.setUpApproved("1", "12", new User(), "0.1"),
                        ExtensionServiceSetUp.setUpApproved("2", "12", new User(), "0.1"),
                        ExtensionServiceSetUp.setUpApproved("3", "12", new User(), "0.1"),
                        ExtensionServiceSetUp.setUpApproved("4", "12", new User(), "0.1"),
                        ExtensionServiceSetUp.setUp("5", "12", new User(), "0.1"),
                        ExtensionServiceSetUp.setUp("6", "12", new User(), "0.1"),
                        ExtensionServiceSetUp.setUp("7", "12", new User(), "0.1"),
                        ExtensionServiceSetUp.setUpApproved("8", "12", new User(), "0.1", true),
                        ExtensionServiceSetUp.setUpApproved("9", "12", new User(), "0.1", true),
                        ExtensionServiceSetUp.setUpApproved("10", "12", new User(), "0.1", 5),
                        ExtensionServiceSetUp.setUpApproved("11", "12", new User(), "0.1", 6),
                        ExtensionServiceSetUp.setUpApproved("12", "12", new User(), "0.1", 3),
                        ExtensionServiceSetUp.setUpApproved("13", "12", new User(), "0.1", true),
                        ExtensionServiceSetUp.setUpApproved("14", "12", new User(), "0.1", true),
                        ExtensionServiceSetUp.setUpApproved("15", "12", new User(), "0.1", true),
                        ExtensionServiceSetUp.setUpApproved("16", "12", new User(), "0.1", true)
                ));

        e = ExtensionServiceSetUp.setUpApproved("1", "12", new User(), "0.1");
        e.setRepoURL("https://github.com/Petroslav/telerik-alpha-final-project");
        GitHubInfo gh = new GitHubInfo();
        gh.setParent(e);
        e.setGitHubInfo(gh);

    }

    @Test
    public void getMostPopularSuccess(){
        List<Extension> mostPop = service.getMostPopular();

        Assert.assertEquals("11", mostPop.get(0).getName());
        Assert.assertEquals("10", mostPop.get(1).getName());
        Assert.assertEquals("12", mostPop.get(2).getName());
    }

    @Test
    public void testAllApproved(){
        List<Extension> approved = service.getAllApproved();

        Assert.assertEquals(13, approved.size());
    }

    @Test
    public void getAdminSelectionSuccess(){
        List<Extension> selection = service.getAdminSelection();

        Assert.assertEquals(6, selection.size());
    }

    @Test
    public void getLatestSuccess(){
        List<Extension> latest = service.getLatest();

        Assert.assertEquals("1", latest.get(0).getName());
        Assert.assertEquals("2", latest.get(1).getName());
        Assert.assertEquals("3", latest.get(2).getName());
        Assert.assertEquals("4", latest.get(3).getName());
    }

    @Test
    public void getAllApprovedSuccess(){
        List<Extension> approved = service.getAllApproved();

        Assert.assertEquals(13, approved.size());
    }

    @Test
    public void getByIdFail(){
        Assert.assertNull(service.getById(-1));
    }
}
