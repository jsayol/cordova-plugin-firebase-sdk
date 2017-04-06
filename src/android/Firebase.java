package org.apache.cordova.firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Firebase extends CordovaPlugin {
    private final String TAG = "Firebase";
    private Context context;
    private FirebaseApp app;

    private final AuthComponent mAuthComponent = new AuthComponent(this);
    private final DatabaseComponent mDatabaseComponent = new DatabaseComponent(this);
    private final MessagingComponent mMessagingComponent = new MessagingComponent(this);
    private final AnalyticsComponent mAnalyticsComponent = new AnalyticsComponent(this);
    private final RemoteConfigComponent mRemoteConfigComponent = new RemoteConfigComponent(this);

    private static boolean inBackground = true;

    public static boolean inBackground() {
        return Firebase.inBackground;
    }

    @Override
    protected void pluginInitialize() {
        context = this.cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.i(TAG, "Execute action " + action);

        if (action.equals("initializeApp")) {
            initializeApp(callbackContext, args.getJSONObject(0));
            return true;
        }

        /* ****************** AUTH ******************* */

        if (action.equals("auth_addAuthStateListener")) {
            mAuthComponent.setCallbackContext(callbackContext);
            return true;
        }

        if (action.equals("auth_removeAuthStateListener")) {
            mAuthComponent.removeCallbackContext();
            return true;
        }

        if (action.equals("auth_signOut")) {
            mAuthComponent.signOut(callbackContext);
            return true;
        }

        if (action.equals("auth_signInAnonymously")) {
            mAuthComponent.signInAnonymously(callbackContext);
            return true;
        }

        if (action.equals("auth_signInWithCustomToken")) {
            mAuthComponent.signInWithCustomToken(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("auth_signInWithEmailAndPassword")) {
            mAuthComponent.signInWithEmailAndPassword(callbackContext, args.getString(0), args.getString(1));
            return true;
        }

        if (action.equals("auth_signInWithCredential")) {
            mAuthComponent.signInWithCredential(callbackContext, args.getJSONObject(0));
            return true;
        }

        if (action.equals("auth_sendPasswordResetEmail")) {
            mAuthComponent.sendPasswordResetEmail(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("auth_verifyPasswordResetCode")) {
            mAuthComponent.verifyPasswordResetCode(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("auth_confirmPasswordReset")) {
            mAuthComponent.confirmPasswordReset(callbackContext, args.getString(0), args.getString(1));
            return true;
        }

        if (action.equals("auth_fetchProvidersForEmail")) {
            mAuthComponent.fetchProvidersForEmail(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("auth_checkActionCode")) {
            mAuthComponent.checkActionCode(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("auth_applyActionCode")) {
            mAuthComponent.applyActionCode(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("auth_createUserWithEmailAndPassword")) {
            mAuthComponent.createUserWithEmailAndPassword(callbackContext, args.getString(0), args.getString(1));
            return true;
        }

        if (action.equals("auth_user_delete")) {
            mAuthComponent.user_delete(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("auth_user_getToken")) {
            mAuthComponent.user_getToken(callbackContext, args.getString(0), args.getBoolean(1));
            return true;
        }

        if (action.equals("auth_user_reload")) {
            mAuthComponent.user_reload(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("auth_user_sendEmailVerification")) {
            mAuthComponent.user_sendEmailVerification(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("auth_user_updateEmail")) {
            mAuthComponent.user_updateEmail(callbackContext, args.getString(0), args.getString(1));
            return true;
        }

        if (action.equals("auth_user_updatePassword")) {
            mAuthComponent.user_updatePassword(callbackContext, args.getString(0), args.getString(1));
            return true;
        }

        if (action.equals("auth_user_updateProfile")) {
            mAuthComponent.user_updateProfile(callbackContext, args.getString(0), args.getJSONObject(1));
            return true;
        }

        if (action.equals("auth_user_link")) {
            mAuthComponent.user_link(callbackContext, args.getString(0), args.getJSONObject(1));
            return true;
        }

        if (action.equals("auth_user_unlink")) {
            mAuthComponent.user_unlink(callbackContext, args.getString(0), args.getString(1));
            return true;
        }

        if (action.equals("auth_user_reauthenticate")) {
            mAuthComponent.user_reauthenticate(callbackContext, args.getString(0), args.getJSONObject(1));
            return true;
        }

        /* ****************** DATABASE ******************* */

        if (action.equals("database_goOffline")) {
            mDatabaseComponent.goOffline(callbackContext);
            return true;
        }

        if (action.equals("database_goOnline")) {
            mDatabaseComponent.goOnline(callbackContext);
            return true;
        }

        if (action.equals("database_setPersistenceEnabled")) {
            mDatabaseComponent.setPersistenceEnabled(callbackContext, args.getBoolean(0));
            return true;
        }

        if (action.equals("database_attachListener")) {
            mDatabaseComponent.attachListener(callbackContext, args.getString(0), args.getBoolean(1),
                    args.getString(2), args.getString(3), args.getJSONObject(4));
            return true;
        }

        if (action.equals("database_removeListeners")) {
            mDatabaseComponent.removeListeners(callbackContext, args.getJSONArray(0));
            return true;
        }

        if (action.equals("database_keepSynced")) {
            mDatabaseComponent.keepSynced(callbackContext, args.getString(0), args.getBoolean(1));
            return true;
        }

        if (action.equals("database_set")) {
            mDatabaseComponent.set(callbackContext, args.getString(0), args.get(1));
            return true;
        }

        if (action.equals("database_update")) {
            mDatabaseComponent.update(callbackContext, args.getString(0), args.getJSONObject(1));
            return true;
        }

        if (action.equals("database_onDisconnect_set")) {
            mDatabaseComponent.onDisconnect_set(callbackContext, args.getString(0), args.get(1));
            return true;
        }

        if (action.equals("database_onDisconnect_update")) {
            mDatabaseComponent.onDisconnect_update(callbackContext, args.getString(0),
                    args.getJSONObject(1));
            return true;
        }

        if (action.equals("database_onDisconnect_cancel")) {
            mDatabaseComponent.onDisconnect_cancel(callbackContext, args.getString(0));
            return true;
        }

        /* ****************** MESSAGING ******************* */

        if (action.equals("messaging_getToken")) {
            mMessagingComponent.getToken(callbackContext);
            return true;
        }

        if (action.equals("messaging_hasPermission")) {
            mMessagingComponent.hasPermission(callbackContext);
            return true;
        }

        if (action.equals("messaging_setBadgeNumber")) {
            mMessagingComponent.setBadgeNumber(callbackContext, args.getInt(0));
            return true;
        }

        if (action.equals("messaging_getBadgeNumber")) {
            mMessagingComponent.getBadgeNumber(callbackContext);
            return true;
        }

        if (action.equals("messaging_subscribe")) {
            mMessagingComponent.subscribe(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("messaging_unsubscribe")) {
            mMessagingComponent.unsubscribe(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("messaging_onNotificationOpen")) {
            mMessagingComponent.onNotificationOpen(callbackContext);
            return true;
        }

        if (action.equals("messaging_onTokenRefresh")) {
            mMessagingComponent.onTokenRefresh(callbackContext);
            return true;
        }

        /* ****************** ANALYTICS ******************* */

        if (action.equals("analytics_logEvent")) {
            mAnalyticsComponent.logEvent(callbackContext, args.getString(0), args.getJSONObject(1));
            return true;
        }

        if (action.equals("analytics_setScreenName")) {
            mAnalyticsComponent.setScreenName(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("analytics_setUserId")) {
            mAnalyticsComponent.setUserId(callbackContext, args.getString(0));
            return true;
        }

        if (action.equals("analytics_setUserProperty")) {
            mAnalyticsComponent.setUserProperty(callbackContext, args.getString(0), args.getString(1));
            return true;
        }

        if (action.equals("remoteConfig_activateFetched")) {
            mRemoteConfigComponent.activateFetched(callbackContext);
            return true;
        }

        /* ****************** REMOTE CONFIG ******************* */

        if (action.equals("remoteConfig_fetch")) {
            mRemoteConfigComponent.fetch(callbackContext, args.optLong(0));
            return true;
        }

        if (action.equals("remoteConfig_getByteArray")) {
            if (args.length() > 1) {
                mRemoteConfigComponent.getByteArray(callbackContext, args.getString(0), args.getString(1));
            } else {
                mRemoteConfigComponent.getByteArray(callbackContext, args.getString(0), null);
            }
            return true;
        }

        if (action.equals("remoteConfig_getValue")) {
            if (args.length() > 1) {
                mRemoteConfigComponent.getValue(callbackContext, args.getString(0), args.getString(1));
            } else {
                mRemoteConfigComponent.getValue(callbackContext, args.getString(0), null);
            }
            return true;
        }

        if (action.equals("remoteConfig_getInfo")) {
            mRemoteConfigComponent.getInfo(callbackContext);
            return true;
        }

        if (action.equals("remoteConfig_setConfigSettings")) {
            mRemoteConfigComponent.setConfigSettings(callbackContext, args.getJSONObject(0));
            return true;
        }

        if (action.equals("remoteConfig_setDefaults")) {
            if (args.length() > 1) {
                mRemoteConfigComponent.setDefaults(callbackContext, args.getJSONObject(0),
                        args.getString(1));
            } else {
                mRemoteConfigComponent.setDefaults(callbackContext, args.getJSONObject(0), null);
            }
            return true;
        }

        return false;
    }

    private void initializeApp(final CallbackContext callbackContext, final JSONObject config) {
        if (app != null) {
            Log.w(TAG, "Tried to initialize app but there's already an app");
            callbackContext.error("Tried to initialize app but there's already an app");
            return;
        }

        try {
            final FirebaseOptions.Builder builder = new FirebaseOptions.Builder();

            if (config.has("apiKey")) {
                builder.setApiKey(config.getString("apiKey"));
            }

            if (config.has("databaseUrl")) {
                builder.setDatabaseUrl(config.getString("databaseUrl"));
            }

            if (config.has("databaseURL")) {
                builder.setDatabaseUrl(config.getString("databaseURL"));
            }

            if (config.has("androidAppId")) {
                builder.setApplicationId(config.getString("androidAppId"));
            }

            if (config.has("storageBucket")) {
                builder.setStorageBucket(config.getString("storageBucket"));
            }

            if (config.has("messagingSenderId")) {
                builder.setGcmSenderId(config.getString("messagingSenderId"));
            }

            final Firebase self = this;

            // Needs to run on the main (UI) thread
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.i(TAG, "Initializing app");
                    app = FirebaseApp.initializeApp(context, builder.build());
                    Log.i(TAG, "App initialized");

                    final Bundle extras = self.cordova.getActivity().getIntent().getExtras();
                    self.cordova.getThreadPool().execute(new Runnable() {
                        public void run() {
                            Log.i(TAG, "Initializing plugin components");
                            mAuthComponent.initialize();
                            mAnalyticsComponent.initialize(context);
                            MessagingComponent.initialize(extras);
                            callbackContext.success();
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error initializing app");
            callbackContext.error(e.getMessage());
        }

    }

    @Override
    public void onPause(boolean multitasking) {
        Log.i(TAG, "To background");
        Firebase.inBackground = true;
    }

    @Override
    public void onResume(boolean multitasking) {
        Log.i(TAG, "To foreground");
        Firebase.inBackground = false;
    }

    @Override
    public void onReset() {
        Log.i(TAG, "Reset");
        mAuthComponent.reset();
        MessagingComponent.reset();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MessagingComponent.sendNotification(intent.getExtras());
    }

}
