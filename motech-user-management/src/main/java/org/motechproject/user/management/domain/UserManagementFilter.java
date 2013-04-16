package org.motechproject.user.management.domain;

import org.motechproject.paginator.contract.FilterParams;

public class UserManagementFilter {

    public static final String USER_NAME = "userName";
    public static final String ROLE = "role";
    private FilterParams filterParams;

    public UserManagementFilter(FilterParams filterParams) {
        this.filterParams = filterParams.removeEmptyParams();
    }

    public String getUserName() {
        return (String) filterParams.get(USER_NAME);
    }

    public String getRole() {
        return (String) filterParams.get(ROLE);
    }

    public boolean hasUserName() {
        return getUserName() != null;
    }

    public boolean hasRole() {
        return getRole() != null;
    }
}
