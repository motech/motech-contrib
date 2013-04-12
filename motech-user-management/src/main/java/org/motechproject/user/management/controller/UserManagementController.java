package org.motechproject.user.management.controller;


import org.motechproject.security.service.MotechUser;
import org.motechproject.user.management.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequestMapping(value = "/userManagement")
public class UserManagementController {

    private UserManagementService userManagementService;

    @Autowired
    public UserManagementController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam String[] userRoles, Model uiModel) {
        ArrayList<MotechUser> users = new ArrayList<>();
        for(String role : userRoles)
            users.addAll(userManagementService.findByRole(role));

        uiModel.addAttribute("users", users);
        return "userManagement/list";
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestParam String userName, @RequestParam String currentPassword, @RequestParam String newPassword) {
        MotechUser motechUser = userManagementService.changePassword(userName, currentPassword, newPassword);
        if(motechUser == null)
            return "Current Password you entered is incorrect";
        return "success";
    }
}
