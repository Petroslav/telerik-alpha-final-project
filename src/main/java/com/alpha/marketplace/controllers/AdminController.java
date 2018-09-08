package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.Properties;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.repositories.base.PropertiesRepository;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ExtensionService extensionService;
    private final PropertiesRepository propertiesRepository;

    @Autowired
    public AdminController(UserService userService, ExtensionService extensionService, PropertiesRepository propertiesRepository) {
        this.userService = userService;
        this.extensionService = extensionService;
        this.propertiesRepository = propertiesRepository;
    }

    @GetMapping("/unapproved")
    @PreAuthorize("hasRole('ADMIN')")
    public String unapproved(Model model){
        if(!userService.getCurrentUser().isAdmin()){
            return "redirect:/";
        }
        List<Extension> extensions = extensionService.getUnapproved();

        model.addAttribute("extensions", extensions);
        model.addAttribute("view", "/admin/unapproved");

        return "base-layout";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String users(Model model){
        if(!userService.getCurrentUser().isAdmin()){
            return "redirect:/";
        }
        List<User> users = userService.getAll();

        model.addAttribute("users", users);
        model.addAttribute("view", "/admin/users");

        return "base-layout";
    }

    @GetMapping("/userEdit")
    @PreAuthorize(("hasRole('ADMIN')"))
    public String editUserRoles(){
        return "redirect:/";
    }

    @PostMapping("/ban/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String banUser(@PathVariable("id") long id){
        if(!userService.banUser(id)){
            System.out.println("Can't ban the guy, he's an admin");
            return "redirect:/admin/users";
        }
        extensionService.reloadLists();
        return "redirect:/user/"+id;
    }

    @PostMapping("/unban/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String unbanUser(Model model, @PathVariable("id") long id){
        userService.unbanUser(id);
        extensionService.reloadLists();
        model.addAttribute("view", "index");

        return "redirect:/user/"+id;
    }

    @GetMapping("/properties")
    @PreAuthorize("hasRole('ADMIN')")
    public String propertiesPage(Model model){
        if(!userService.getCurrentUser().isAdmin()){
            return "redirect:/";
        }
        boolean hasFailed = false;

        Properties properties = propertiesRepository.get();

        String success = properties.getLastSuccessfulSync().toString();
        Date failDate = properties.getLastFailedSync();

        String fail = null;

        if(failDate == null){
            fail = "Never";
        }else {
            fail = failDate.toString();
            hasFailed = true;
        }
        String failInfo = properties.getFailInfo();

        model.addAttribute("hasFailed", hasFailed);
        model.addAttribute("success", success);
        model.addAttribute("fail", fail);
        model.addAttribute("failInfo", failInfo);
        model.addAttribute("properties", properties);
        model.addAttribute("view", "/admin/properties");
        return "base-layout";
    }

    @PostMapping("/editProperties")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProperties(@RequestParam("delay") long delay){
        if(!userService.getCurrentUser().isAdmin()){
            return "redirect:/";
        }

        extensionService.setSync(delay);
        return "redirect:/admin/properties";
    }

    @PostMapping("/syncAll")
    @PreAuthorize("hasRole('ADMIN')")
    public String syncAll(){
        if(!userService.getCurrentUser().isAdmin()){
            return "redirect:/";
        }

        extensionService.syncAllExtensions();
        return "redirect:/admin/properties";
    }
    @PostMapping("/approveSelected")
    public String removeFeatured(@RequestParam("list") List<Long> stuff) {
        extensionService.approveList(stuff);

        return "redirect:/";
    }
}
