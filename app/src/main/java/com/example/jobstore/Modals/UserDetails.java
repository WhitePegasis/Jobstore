package com.example.jobstore.Modals;

public class UserDetails {
    private String name,email,adminPageCode,lastEnteredJobStoreId;
    private boolean isAdmin;

    public UserDetails(String name, String email) {
        this.name = name;
        this.email = email;
        this.isAdmin=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getAdminPageCode() {
        return adminPageCode;
    }

    public void setAdminPageCode(String adminPageCode) {
        this.adminPageCode = adminPageCode;
    }

    public String getLastEnteredJobStoreId() {
        return lastEnteredJobStoreId;
    }

    public void setLastEnteredJobStoreId(String lastEnteredJobStoreId) {
        this.lastEnteredJobStoreId = lastEnteredJobStoreId;
    }
}
