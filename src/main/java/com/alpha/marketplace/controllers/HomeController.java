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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public String index(Model model, @RequestParam(value = "sort", required = false) String sort) {
        List<Extension> newestExtensions = extensionService.getLatest();
        List<Extension> selectedByAdmin = extensionService.getAdminSelection();
        List<Extension> mostPopular = extensionService.getMostPopular();

        if (sort != null) {
            switch (sort) {

                case "byLastCommit":
                    newestExtensions.sort((e1, e2) -> e2.getGitHubInfo().getLastCommit().compareTo(e1.getGitHubInfo().getLastCommit()));
                    selectedByAdmin.sort((e1, e2) -> e2.getGitHubInfo().getLastCommit().compareTo(e1.getGitHubInfo().getLastCommit()));
                    mostPopular.sort((e1, e2) -> e2.getGitHubInfo().getLastCommit().compareTo(e1.getGitHubInfo().getLastCommit()));
                    break;

                case "byUploadDate":
                    newestExtensions.sort((e1, e2) -> e2.getAddedOn().compareTo(e1.getAddedOn()));
                    selectedByAdmin.sort((e1, e2) -> e2.getAddedOn().compareTo(e1.getAddedOn()));
                    mostPopular.sort((e1, e2) -> e2.getAddedOn().compareTo(e1.getAddedOn()));
                    break;

                case "byDownloads":
                    newestExtensions.sort((e1, e2) -> e2.getDownloads() - e1.getDownloads() == 0 ? e1.getName().compareTo(e2.getName()) : e2.getDownloads() - e1.getDownloads());
                    selectedByAdmin.sort((e1, e2) -> e2.getDownloads() - e1.getDownloads() == 0 ? e1.getName().compareTo(e2.getName()) : e2.getDownloads() - e1.getDownloads());
                    mostPopular.sort((e1, e2) -> e2.getDownloads() - e1.getDownloads() == 0 ? e1.getName().compareTo(e2.getName()) : e2.getDownloads() - e1.getDownloads());
                    break;
                default:
                    break;
            }
        }

        model.addAttribute("view", "index");
        model.addAttribute("newest", newestExtensions);
        model.addAttribute("adminSelection", selectedByAdmin);
        model.addAttribute("mostPopular", mostPopular);

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
    public String regUser(@Valid @ModelAttribute UserBindingModel user, BindingResult errors, Model model) {
        service.registerUser(user, errors);
        if (errors.hasErrors()) {
            model.addAttribute("view", "register");
            return "base-layout";
        }

        return "redirect:/";
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
    public String search(@RequestParam(value = "criteria", required = false) String criteria, Model model) {
        Set<Extension> matches;
        boolean isUserSearch = false;
        if (criteria.startsWith("user:")) {
            List<User> matchedUsers = service.searchUsers(criteria.substring(5));
            model.addAttribute("userMatches", matchedUsers);
            isUserSearch = true;
        } else {
            matches = new HashSet<>(extensionService.searchExtensions(criteria));
            matches.addAll(extensionService.searchExtensionsByTag(criteria));
            model.addAttribute("matches", matches);
        }
        model.addAttribute("isUserSearch", isUserSearch);
        model.addAttribute("view", "/search");
        return "base-layout";
    }
}
