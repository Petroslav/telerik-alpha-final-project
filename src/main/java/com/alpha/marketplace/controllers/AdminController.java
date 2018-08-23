package com.alpha.marketplace.controllers;

import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ExtensionService extensionService;

    @Autowired
    public AdminController(UserService userService, ExtensionService extensionService) {
        this.userService = userService;
        this.extensionService = extensionService;
    }
}
