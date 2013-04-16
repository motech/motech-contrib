package org.motechproject.user.management.service;

import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.paginator.service.Paging;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.exceptions.WebSecurityException;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.user.management.domain.UserManagementFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Service
public class UserManagementService implements Paging<MotechWebUser> {
    private MotechAuthenticationService motechAuthenticationService;
    private AllMotechWebUsers allMotechWebUsers;

    @Autowired
    public UserManagementService(MotechAuthenticationService motechAuthenticationService, AllMotechWebUsers allMotechWebUsers) {
        this.motechAuthenticationService = motechAuthenticationService;
        this.allMotechWebUsers = allMotechWebUsers;
    }

    public List<MotechUser> findByRole(String role) {
        return motechAuthenticationService.findByRole(role);
    }

    public boolean activateUser(String userName) {
         return motechAuthenticationService.activateUser(userName);
    }

    public boolean removeUser(String userName) {
        return motechAuthenticationService.remove(userName);
    }

    public boolean resetPassword(String userName, String newPassword) {
        return motechAuthenticationService.resetPassword(userName, newPassword);
    }

    @Override
    public PageResults page(Integer pageNo, Integer rowsPerPage, FilterParams filterCriteria, SortParams sortCriteria) {
        int startIndex = (pageNo - 1) * rowsPerPage;

        UserManagementFilter filter = new UserManagementFilter(filterCriteria);
        List<MotechWebUser> results;
        int totalCount = 0;
        if(filter.hasUserName()) {
            results = getResultsForUserName(filter);
            totalCount = results.size();
        }
        else if(filter.hasRole()){
            results = allMotechWebUsers.findByRole(filter.getRole(), startIndex, rowsPerPage);
            totalCount = allMotechWebUsers.countByRole(filter.getRole());

        } else {
            results = allMotechWebUsers.findAllUsers(startIndex, rowsPerPage);
            totalCount = allMotechWebUsers.countAllUsers();
        }

        PageResults pageResults = new PageResults();
        pageResults.setResults(results);
        pageResults.setTotalRows(totalCount);
        return pageResults;
    }

    private List<MotechWebUser> getResultsForUserName(UserManagementFilter userManagementFilter) {
        MotechWebUser webUser = allMotechWebUsers.findByUserName(userManagementFilter.getUserName());
        if(webUser != null) {
            return asList(webUser);
        } else {
            return  new ArrayList();
        }
    }

    @Override
    public String entityName() {
        return "UserManagement";
    }

    public boolean deactivate(String userName) {
        return motechAuthenticationService.deactivateUser(userName);
    }

    public boolean addNewUser(MotechWebUser motechWebUser) throws WebSecurityException {
        String userName = motechWebUser.getUserName();
        String password = motechWebUser.getPassword();
        String externalId = motechWebUser.getExternalId();
        List<String> roles = motechWebUser.getRoles();
        motechAuthenticationService.register(userName, password, externalId, roles);
        return true;
    }
}
