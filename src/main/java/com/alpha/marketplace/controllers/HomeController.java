package com.alpha.marketplace.controllers;

import com.alpha.marketplace.exceptions.ErrorMessages;
import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.GitHubInfo;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
@Controller
public class HomeController {

    private final ExtensionService extensionService;
    private final UserService service;

    @Autowired
    public HomeController(ExtensionService extensionService, UserService service) {
        this.extensionService = extensionService;
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {

        List<Extension> newestExtensions = extensionService.getLatest().stream().limit(5).collect(Collectors.toList());
        List<Extension> selectedByAdmin = extensionService.getAdminSelection();
        List<Extension> mostPopular = extensionService.getMostPopular().stream().limit(5).collect(Collectors.toList());

        model.addAttribute("view", "index");
        model.addAttribute("newest", newestExtensions);
        model.addAttribute("adminSelection", selectedByAdmin);
        model.addAttribute("mostPopular", mostPopular);

        return "base-layout";
    }

    @GetMapping("/newest")
    public String newestPage(Model model, @RequestParam(value = "sort", required = false) String sort) {
        List<Extension> extensions = extensionService.getLatest();

        switch (sort) {

            case "byLastCommit":
                extensions.sort((e1, e2) -> e2.getGitHubInfo().getLastCommit().compareTo(e1.getGitHubInfo().getLastCommit()));
                break;

            case "byUploadDate":
                extensions.sort((e1, e2) -> e2.getAddedOn().compareTo(e1.getAddedOn()));
                break;

            case "byDownloads":
                extensions.sort((e1, e2) -> e2.getDownloads() - e1.getDownloads() == 0 ? e1.getName().compareTo(e2.getName()) : e2.getDownloads() - e1.getDownloads());
                break;
            default:
                break;
        }

        model.addAttribute("extensions", extensions);
        model.addAttribute("view", "home/newest");
        return "base-layout";
    }

    @GetMapping("/popular")
    public String mostPopularPage(Model model, @RequestParam(value = "sort", required = false) String sort) {
        List<Extension> mostPopular = extensionService.getMostPopular();

        switch (sort) {
            case "byLastCommit":
                mostPopular.sort((e1, e2) -> {
                    if(e1.getGitHubInfo() != null) {
                        return e2.getGitHubInfo().getLastCommit().compareTo(e1.getGitHubInfo().getLastCommit());
                    }else{
                        return -1;
                    }

                });
                break;

            case "byUploadDate":
                mostPopular.sort((e1, e2) -> e2.getAddedOn().compareTo(e1.getAddedOn()));
                break;

            case "byDownloads":
                mostPopular.sort((e1, e2) -> e2.getDownloads() - e1.getDownloads() == 0 ? e1.getName().compareTo(e2.getName()) : e2.getDownloads() - e1.getDownloads());
                break;
        }

        model.addAttribute("extensions", mostPopular);
        model.addAttribute("view", "home/popular");
        return "base-layout";
    }

    @GetMapping("/register")
    public String register(Model model) {
        if (!Utils.userIsAnonymous()) {
            return "redirect:/";
        }
        model.addAttribute("view", "register");
        model.addAttribute("user", new UserBindingModel());
        return "base-layout";
    }

    @PostMapping("/register")
    public String regUser(@Valid @ModelAttribute("user") UserBindingModel user, BindingResult errors, Model model) {
        service.registerUser(user, errors);
        if (errors.hasErrors()) {
            model.addAttribute("errors", errors);
            model.addAttribute("view", "register");

            return "base-layout";
        }
        service.reloadMemory();

        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String login(Model model, @RequestParam(required = false) String error) {
        if (!Utils.userIsAnonymous()) {
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("error", ErrorMessages.INVALID_CREDENTIALS);
        }
        model.addAttribute("view", "login");

        return "base-layout";
    }

    @GetMapping("/unauthorized")
    public String unauthorized(Model model) {
        model.addAttribute("view", "unauthorized");

        return "base-layout";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "criteria", required = false) String criteria, @RequestParam(value = "sort", required = false) String sort, Model model) {
        if(sort==null){
            return "redirect:/search?criteria="+criteria+"&sort=byDownloads";
        }
        Set<Extension> matches;
        List<User> matchedUsers = service.searchUsers(criteria);
        model.addAttribute("userMatches", matchedUsers);

        matches = new HashSet<>(extensionService.searchExtensions(criteria));
        matches.addAll(extensionService.searchExtensionsByTag(criteria));

        List<Extension> extensions = new ArrayList<>(matches);
        if (!matches.isEmpty()) {
            switch (sort) {
                case "byLastCommit":
                    extensions.sort((e1, e2) -> e2.getGitHubInfo().getLastCommit().compareTo(e1.getGitHubInfo().getLastCommit()));
                    break;

                case "byUploadDate":
                    extensions.sort((e1, e2) -> e2.getAddedOn().compareTo(e1.getAddedOn()));
                    break;

                case "byDownloads":
                    extensions.sort((e1, e2) -> e2.getDownloads() - e1.getDownloads() == 0 ? e1.getName().compareTo(e2.getName()) : e2.getDownloads() - e1.getDownloads());
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("matches", extensions);


        model.addAttribute("view", "/search");
        return "base-layout";
    }


}
