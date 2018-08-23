package com.alpha.marketplace.controllers;

import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    private final UserService service;

    @Autowired
    public OwnerController(UserService service) {
        this.service = service;
    }


}
