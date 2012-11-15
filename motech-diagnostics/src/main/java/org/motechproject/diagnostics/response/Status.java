package org.motechproject.diagnostics.response;

public enum Status {
    Success(0), Warn(1), Fail(2), Unknown(4);
    private int level;

    Status(int level) {
        this.level = level;
    }

    public int level() {
        return level;
    }

    public static Status status(int level) {
        for(Status status : Status.values()){
            if(status.level() == level){
                return status;
            }
        }
        return null;
    }
}
