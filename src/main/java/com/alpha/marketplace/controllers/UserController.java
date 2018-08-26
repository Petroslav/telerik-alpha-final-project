package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.models.edit.UserEditModel;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/public/register")
    public String regUser(Model model, @Valid @ModelAttribute UserBindingModel user, BindingResult errors){

        User newUser = service.registerUser(user, errors);
        if(errors.hasErrors()){
            return "register";
        }
        model.addAttribute("user", newUser);
        model.addAttribute("view", "successfulReg");

        return "base-layout";
    }
    @GetMapping("/{id}")
    public String userDetails(Model model, @PathVariable("id") Integer id){
        User user = service.findById(id);
        if(user == null){
            model.addAttribute("view", "error/404");
            return "base-layout";
        }
        model.addAttribute("user", user);
        model.addAttribute("view", "user/details");

        return "base-layout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        User u = getUser();

        List<Extension> extensions = u.getExtensions();


        model.addAttribute("user", u);
        model.addAttribute("extensions", extensions);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }

    @PostMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(Model model, @ModelAttribute UserEditModel userEdit){
        User u = getUser();
        service.editUser(u, userEdit);
        return  "redirect:/profile";
    }


    @PostMapping("/update/pic/file")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(Model model, @RequestParam("picFile") MultipartFile file){
        User u = getUser();
        service.editUserPic(u, file);
        //TODO HANDLE ERROR IF FILE TOO BIG OR BAD FILE FORMAT

        return  "redirect:/profile";
    }

    @PostMapping("/update/pic/url")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(Model model, @RequestParam("picUrl") String urlString){
        User u = getUser();
        service.editUserPic(u, urlString);
        //TODO HANDLE ERROR IF BAD URL OR TOO BIG
        return  "redirect:/profile";
    }

    private User getUser(){
        UserDetails user = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return (User)service.loadUserByUsername(user.getUsername());
    }

    @GetMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    public String editProfilePage(Model model){
        if (Utils.userIsAnonymous()) {
            return "redirect:/";
        }
        model.addAttribute("view", "user/edit");
        return "base-layout";
    }

    @PostMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    public String editProfile(Model model, UserEditModel editModel){

        service.editUser(service.currentUser(), editModel);

        return "redirect:/user/profile";
    }
}
