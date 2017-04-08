package org.apache.cordova.firebase;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import android.util.Log;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.ActionCodeResult;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.auth.AuthCredential;

import java.util.List;

public class AuthComponent {
    private static final String TAG = "FirebaseAuth";

    private Firebase mFirebase;
    private FirebaseAuth mFirebaseAuth = null;
    private FirebaseAuth.AuthStateListener mAuthListener = null;
    private FirebaseUser mFirebaseUser = null;
    private CallbackContext authStateCallbackContext = null;

    public AuthComponent(final Firebase plugin, final FirebaseApp firebaseApp) {
        Log.i(TAG, "New instance, initializing");
        mFirebase = plugin;
        mFirebaseAuth = FirebaseAuth.getInstance(firebaseApp);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                try {
                    mFirebaseUser = firebaseAuth.getCurrentUser();

                    if (mFirebaseUser != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + mFirebaseUser.getUid());
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }

                    if (authStateCallbackContext != null) {
                        sendCurrentUser();
                    }
                } catch (Exception e) {
                    authStateCallbackContext.error(e.getMessage());
                }
            }
        };

        Log.i(TAG, "Attaching auth state listener");
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    public void reset() {
        Log.i(TAG, "Resetting");
        if ((mFirebaseAuth != null) && (mAuthListener != null)) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void setCallbackContext(final CallbackContext callbackContext) {
        authStateCallbackContext = callbackContext;
    }

    public void removeCallbackContext() {
        authStateCallbackContext = null;
    }

    public void signOut(final CallbackContext callbackContext) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Signing out");
                    mFirebaseAuth.signOut();
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error signing out", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void signInAnonymously(final CallbackContext callbackContext) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Signing in anonymously");
                    handleAuthTask(callbackContext, mFirebaseAuth.signInAnonymously());
                } catch (Exception e) {
                    Log.e(TAG, "Error signing in anonymously", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void signInWithCustomToken(final CallbackContext callbackContext, final String token) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Signing in with custom token");
                    handleAuthTask(callbackContext, mFirebaseAuth.signInWithCustomToken(token));
                } catch (Exception e) {
                    Log.e(TAG, "Error signing in with custom token", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void signInWithEmailAndPassword(final CallbackContext callbackContext, final String email, final String password) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Signing in with email and password");
                    handleAuthTask(callbackContext, mFirebaseAuth.signInWithEmailAndPassword(email, password));
                } catch (Exception e) {
                    Log.e(TAG, "Error signing in with email and password", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void signInWithCredential(final CallbackContext callbackContext, final JSONObject credential) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Signing in with credential");
                    final AuthCredential authCredential = AuthCredentials.getCredential(credential);
                    handleAuthTask(callbackContext, mFirebaseAuth.signInWithCredential(authCredential));
                } catch (Exception e) {
                    Log.e(TAG, "Error signing in with credential", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void sendPasswordResetEmail(final CallbackContext callbackContext, final String email) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Sending password reset email");
                    mFirebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        callbackContext.success();
                                    } else {
                                        Exception e = task.getException();
                                        Log.w(TAG, "Failure sending password reset email", e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error sending password reset email", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void verifyPasswordResetCode(final CallbackContext callbackContext, final String code) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Verifying password reset code");
                    mFirebaseAuth.verifyPasswordResetCode(code)
                            .addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(Task<String> task) {
                                    if (task.isSuccessful()) {
                                        callbackContext.success(task.getResult());
                                    } else {
                                        Exception e = task.getException();
                                        Log.w(TAG, "Failure verifying password reset code", e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error verifying password reset code", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void confirmPasswordReset(final CallbackContext callbackContext, final String code, final String newPassword) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Confirming password reset");
                    mFirebaseAuth.confirmPasswordReset(code, newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        callbackContext.success();
                                    } else {
                                        Exception e = task.getException();
                                        Log.w(TAG, "Failure confirming password reset", e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error confirming password reset", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void fetchProvidersForEmail(final CallbackContext callbackContext, final String email) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Fetching providers for email");
                    mFirebaseAuth.fetchProvidersForEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(Task<ProviderQueryResult> task) {
                                    if (task.isSuccessful()) {
                                        List<String> providersList = task.getResult().getProviders();
                                        callbackContext.success(new JSONArray(providersList));
                                    } else {
                                        Exception e = task.getException();
                                        Log.w(TAG, "Failure fetching providers for email", e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error fetching providers for email", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void checkActionCode(final CallbackContext callbackContext, final String code) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Checking action code");
                    mFirebaseAuth.checkActionCode(code)
                            .addOnCompleteListener(new OnCompleteListener<ActionCodeResult>() {
                                @Override
                                public void onComplete(Task<ActionCodeResult> task) {
                                    if (task.isSuccessful()) {
                                        ActionCodeResult result = task.getResult();
                                        String email;
                                        if (result != null) {
                                            email = (String) result.getData(ActionCodeResult.EMAIL);
                                        } else {
                                            email = null;
                                        }
                                        callbackContext.success(email);
                                    } else {
                                        Exception e = task.getException();
                                        Log.w(TAG, "Failure checking action code", e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error checking action code", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void applyActionCode(final CallbackContext callbackContext, final String code) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Applying action code");
                    mFirebaseAuth.applyActionCode(code)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        callbackContext.success();
                                    } else {
                                        Exception e = task.getException();
                                        Log.w(TAG, "Failure applying action code", e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error applying action code", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void createUserWithEmailAndPassword(final CallbackContext callbackContext, final String email, final String password) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Creating user with email and password");
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    try {
                                        if (task.isSuccessful()) {
                                            AuthResult result = task.getResult();
                                            JSONObject user = firebaseUserToJSONObject(result.getUser());
                                            callbackContext.success(user);
                                        } else {
                                            Exception e = task.getException();
                                            Log.w(TAG, "Failure creating user with email and password", e);
                                            callbackContext.error(e.getMessage());
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error creating user with email and password", e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error creating user with email and password", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_delete(final CallbackContext callbackContext, final String uid) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Deleting user " + uid);
                    assertCurrentUser(uid);
                    mFirebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                callbackContext.success();
                            } else {
                                Exception e = task.getException();
                                Log.w(TAG, "Failure deleting user " + uid, e);
                                callbackContext.error(e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error deleting user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_getToken(final CallbackContext callbackContext, final String uid, final boolean forceRefresh) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Getting token for user " + uid);
                    assertCurrentUser(uid);
                    mFirebaseUser.getToken(forceRefresh)
                            .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                @Override
                                public void onComplete(Task<GetTokenResult> task) {
                                    if (task.isSuccessful()) {
                                        GetTokenResult result = task.getResult();
                                        String token;
                                        if (result != null) {
                                            token = (String) result.getToken();
                                        } else {
                                            token = null;
                                        }
                                        callbackContext.success(token);
                                    } else {
                                        Exception e = task.getException();
                                        Log.w(TAG, "Failure getting token for user " + uid, e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error getting token for user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_reload(final CallbackContext callbackContext, final String uid) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Deleting user " + uid);
                    assertCurrentUser(uid);
                    mFirebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                try {
                                    callbackContext.success(firebaseUserToJSONObject(mFirebaseUser));
                                } catch (Exception e) {
                                    Log.e(TAG, "Error converting user to JSON", e);
                                    callbackContext.error(e.getMessage());
                                }
                            } else {
                                Exception e = task.getException();
                                Log.w(TAG, "Failure deleting user " + uid, e);
                                callbackContext.error(e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error deleting user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_sendEmailVerification(final CallbackContext callbackContext, final String uid) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Send email verification for user " + uid);
                    assertCurrentUser(uid);
                    mFirebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                callbackContext.success();
                            } else {
                                Exception e = task.getException();
                                Log.w(TAG, "Failure send email verification for user " + uid, e);
                                callbackContext.error(e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error send email verification for user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_updateEmail(final CallbackContext callbackContext, final String uid, final String email) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Updating email for user " + uid);
                    assertCurrentUser(uid);
                    mFirebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                callbackContext.success();
                            } else {
                                Exception e = task.getException();
                                Log.w(TAG, "Failure updating email for user " + uid, e);
                                callbackContext.error(e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error updating email for user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_updatePassword(final CallbackContext callbackContext, final String uid, final String password) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Updating password for user " + uid);
                    assertCurrentUser(uid);
                    mFirebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                callbackContext.success();
                            } else {
                                Exception e = task.getException();
                                Log.w(TAG, "Failure updating password for user " + uid, e);
                                callbackContext.error(e.getMessage());
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error updating password for user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_updateProfile(final CallbackContext callbackContext, final String uid, final JSONObject profile) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Updating profile for user " + uid);
                    assertCurrentUser(uid);

                    final UserProfileChangeRequest.Builder requestBuilder = new UserProfileChangeRequest.Builder();
                    if (profile.has("displayName")) {
                        requestBuilder.setDisplayName(profile.getString("displayName"));
                    }
                    if (profile.has("photoURL")) {
                        requestBuilder.setPhotoUri(Uri.parse(profile.getString("photoURL")));
                    }

                    mFirebaseUser.updateProfile(requestBuilder.build())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        callbackContext.success();
                                    } else {
                                        Exception e = task.getException();
                                        Log.w(TAG, "Failure updating profile for user " + uid, e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e(TAG, "Error updating profile for user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_link(final CallbackContext callbackContext, final String uid, final JSONObject credential) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Linking with credential for user " + uid);
                    assertCurrentUser(uid);
                    final AuthCredential authCredential = AuthCredentials.getCredential(credential);
                    handleLinkAuthtask(callbackContext, mFirebaseUser.linkWithCredential(authCredential));
                } catch (Exception e) {
                    Log.e(TAG, "Error linking with credential for user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_unlink(final CallbackContext callbackContext, final String uid, final String providerId) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Unlinking provider " + providerId + " for user " + uid);
                    assertCurrentUser(uid);
                    handleLinkAuthtask(callbackContext, mFirebaseUser.unlink(providerId));
                } catch (Exception e) {
                    Log.e(TAG, "Error unlinking provider " + providerId + " for user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void user_reauthenticate(final CallbackContext callbackContext, final String uid, final JSONObject credential) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Reauthenticating with credential for user " + uid);
                    assertCurrentUser(uid);
                    final AuthCredential authCredential = AuthCredentials.getCredential(credential);
                    mFirebaseUser.reauthenticate(authCredential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        callbackContext.success();
                                    } else {
                                        Exception e = task.getException();
                                        Log.w(TAG, "Failure reauthenticating with credential for user " + uid, e);
                                        callbackContext.error(e.getMessage());
                                    }
                                }
                            });

                } catch (Exception e) {
                    Log.e(TAG, "Error reauthenticating with credential for user " + uid, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private void handleAuthTask(final CallbackContext callbackContext, Task<AuthResult> task) {
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {
                        AuthResult result = task.getResult();
                        JSONObject user = firebaseUserToJSONObject(result.getUser());
                        callbackContext.success(user);
                    } else {
                        Exception e = task.getException();
                        Log.w(TAG, "Failure signing in", e);
                        PluginResult pluginresult = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
                        pluginresult.setKeepCallback(true);
                        authStateCallbackContext.sendPluginResult(pluginresult);
                        callbackContext.error(e.getMessage());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error handling auth onComplete", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private void handleLinkAuthtask(final CallbackContext callbackContext, Task<AuthResult> task) {
        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    try {
                        mFirebaseUser = task.getResult().getUser();
                        callbackContext.success(firebaseUserToJSONObject(mFirebaseUser));
                    } catch (Exception e) {
                        Log.e(TAG, "Error converting user to JSON", e);
                        callbackContext.error(e.getMessage());
                    }
                } else {
                    Exception e = task.getException();
                    Log.w(TAG, "Failure during credential auth task", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private void sendCurrentUser() throws Exception {
        JSONObject currentUser = firebaseUserToJSONObject(mFirebaseUser);
        PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, currentUser);
        pluginresult.setKeepCallback(true);
        authStateCallbackContext.sendPluginResult(pluginresult);
    }

    private void assertCurrentUser(final String uid) throws Exception {
        if (mFirebaseUser == null) {
            throw new Error("There is no user currently signed-in");
        }

        if (!mFirebaseUser.getUid().equals(uid)) {
            throw new Exception("The provided user doesn't correspond with the user currently signed-in");
        }
    }

    private static JSONObject firebaseUserToJSONObject(FirebaseUser firebaseUser) throws Exception {
        JSONObject jsonUser = userInfoToJSONObject((UserInfo) firebaseUser);
        jsonUser.put("isAnonymous", firebaseUser.isAnonymous());

        JSONArray providerDataArray = new JSONArray();
        for (UserInfo userInfo : firebaseUser.getProviderData()) {
            providerDataArray.put(userInfoToJSONObject(userInfo));
        }
        jsonUser.put("providerData", providerDataArray);

        return jsonUser;
    }

    private static JSONObject userInfoToJSONObject(UserInfo userInfo) throws Exception {
        Uri photoUrl = userInfo.getPhotoUrl();

        JSONObject jsonUser = new JSONObject();
        jsonUser.put("uid", userInfo.getUid());
        jsonUser.put("displayName", userInfo.getDisplayName());
        jsonUser.put("email", userInfo.getEmail());
        jsonUser.put("photoURL", (photoUrl != null) ? photoUrl.toString() : null);
        jsonUser.put("providerId", userInfo.getProviderId());
        jsonUser.put("emailVerified", userInfo.isEmailVerified());
        return jsonUser;
    }

}
