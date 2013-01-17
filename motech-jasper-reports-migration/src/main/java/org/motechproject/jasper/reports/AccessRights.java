package org.motechproject.jasper.reports;

public enum AccessRights {
    NO_ACCESS(0),
    ADMINISTER(1),
    READ_ONLY(2),
    READ_DELETE(18),
    READ_WRITE_DELETE(30),
    EXECUTE_ONLY(32);

    private  int permissionMask;

    private AccessRights(int permissionMask) {
        this.permissionMask = permissionMask;
    }

    public int getPermissionMask() {
        return permissionMask;
    }
}
