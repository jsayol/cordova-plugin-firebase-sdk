package org.apache.cordova.firebase;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.GithubAuthCredential;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.TwitterAuthCredential;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;

import org.json.JSONObject;

public class AuthCredentials {
    public static AuthCredential getCredential(final JSONObject credential) throws Exception {
        final String providerId = credential.getString("provider");
        AuthCredential authCredential;

        if (providerId.equals(EmailAuthProvider.PROVIDER_ID)) {
            authCredential = getEmailAuthCredential(credential);
        } else if (providerId.equals(FacebookAuthProvider.PROVIDER_ID)) {
            authCredential = getFacebookAuthCredential(credential);
        } else if (providerId.equals(GithubAuthProvider.PROVIDER_ID)) {
            authCredential = getGithubAuthCredential(credential);
        } else if (providerId.equals(GoogleAuthProvider.PROVIDER_ID)) {
            authCredential = getGoogleAuthCredential(credential);
        } else if (providerId.equals(TwitterAuthProvider.PROVIDER_ID)) {
            authCredential = getTwitterAuthCredential(credential);
        } else {
            throw new Exception("Unknown provider ID: " + providerId);
        }

        return authCredential;
    }

    private static AuthCredential getEmailAuthCredential(final JSONObject credential) throws Exception {
        final String email = credential.getString("email");
        final String password = credential.getString("password");
        return EmailAuthProvider.getCredential(email, password);
    }

    private static AuthCredential getFacebookAuthCredential(final JSONObject credential) throws Exception {
        final String token = credential.getString("token");
        return FacebookAuthProvider.getCredential(token);
    }

    private static AuthCredential getGithubAuthCredential(final JSONObject credential) throws Exception {
        final String token = credential.getString("token");
        return GithubAuthProvider.getCredential(token);
    }

    private static AuthCredential getGoogleAuthCredential(final JSONObject credential) throws Exception {
        final String idToken = credential.optString("idToken");
        final String accessToken = credential.optString("accessToken");
        return GoogleAuthProvider.getCredential(idToken, accessToken);
    }

    private static AuthCredential getTwitterAuthCredential(final JSONObject credential) throws Exception {
        final String token = credential.getString("token");
        final String secret = credential.getString("secret");
        return TwitterAuthProvider.getCredential(token, secret);
    }
}
