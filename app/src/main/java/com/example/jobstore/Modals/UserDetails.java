package com.example.jobstore.Modals;

public class UserDetails {
    private String name,email, currentJobStoreKey,uid, myStoreKey, uniqueId;

    public UserDetails() {
    }

    public UserDetails( String email, String uid) {
        this.name = "name";
        this.email = email;
        this.uid = uid;
        this.myStoreKey="";
        this.currentJobStoreKey="";
    }

    public String getCurrentJobStoreKey() {
        return currentJobStoreKey;
    }

    public void setCurrentJobStoreKey(String currentJobStoreKey) {
        this.currentJobStoreKey = currentJobStoreKey;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMyStoreKey() {
        return myStoreKey;
    }

    public void setMyStoreKey(String myStoreKey) {
        this.myStoreKey = myStoreKey;
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


    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}
