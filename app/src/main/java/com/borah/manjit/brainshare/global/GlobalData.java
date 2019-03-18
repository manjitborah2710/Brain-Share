package com.borah.manjit.brainshare.global;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GlobalData extends Application {
    public FirebaseAuth authToken;
    public FirebaseUser user;

    public FirebaseAuth getAuthToken() {
        return authToken;
    }

    public void setAuthToken(FirebaseAuth authToken) {
        this.authToken = authToken;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }
}
