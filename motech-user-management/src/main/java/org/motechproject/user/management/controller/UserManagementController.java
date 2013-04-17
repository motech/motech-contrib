package org.motechproject.user.management.controller;


import org.apache.log4j.Logger;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.user.management.domain.UserRoles;
import org.motechproject.user.management.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/userManagement")
public class UserManagementController {
    protected Logger logger = Logger.getLogger(this.getClass().getName());

    private UserManagementService userManagementService;
    private UserRoles userRoles;

    @Autowired
    public UserManagementController(UserManagementService userManagementService, UserRoles userRoles) {
        this.userManagementService = userManagementService;
        this.userRoles = userRoles;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model uiModel) {
        uiModel.addAttribute("roles", userRoles.all());
        return "userManagement/list";
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public String list(@RequestParam String userName, @RequestParam String newPassword) {
        boolean updated = userManagementService.resetPassword(userName, newPassword);
        if (updated)
            return "Password reset successfully";
        else
            return "Could not update password";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/activateUser")
    @ResponseBody
    public String activateUser(@RequestParam String userName, @RequestParam String newPassword) {
        boolean hasResetPassword = userManagementService.resetPassword(userName, newPassword);
        if (hasResetPassword)
            if (userManagementService.activateUser(userName)) {
                return "User has been activated";
            } else {
                return "User could not be activated";
            }
        else
            return "User does not exist";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/deactivateUser")
    @ResponseBody
    public String deactivateUser(@RequestParam String userName) {
        if (userManagementService.deactivate(userName)) {
            return "User has been de-activated";
        } else {
            return "User could not be de-activated";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/removeUser")
    @ResponseBody
    public String removeUser(@RequestParam String userName) {
        if (userManagementService.removeUser(userName)) {
            return "success";
        } else {
            return "error";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addNewUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String createUser(@RequestBody MotechWebUser motechWebUser) throws WebSecurityException {
        userManagementService.addNewUser(motechWebUser);
        return "User has been created";
    }


    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) {
        logger.error(ex.getMessage());
    }

}
