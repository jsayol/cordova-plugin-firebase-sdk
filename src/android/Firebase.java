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

import java.util.HashMap;

public class Firebase extends CordovaPlugin {
    private final String TAG = "Firebase";
    private Context context;

    private final MessagingComponent mMessagingComponent = new MessagingComponent(this);
    private final AnalyticsComponent mAnalyticsComponent = new AnalyticsComponent(this);
    private final RemoteConfigComponent mRemoteConfigComponent = new RemoteConfigComponent(this);

    private HashMap<String, AuthComponent> appAuth = new HashMap<String, AuthComponent>();
    private HashMap<String, DatabaseComponent> appDatabase = new HashMap<String, DatabaseComponent>();

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

        try {
            if (action.equals("initializeApp")) {
                initializeApp(callbackContext, args.getString(0), args.getJSONObject(1));
                return true;
            }

            if (action.equals("deleteApp")) {
                deleteApp(callbackContext, args.getString(0));
                return true;
            }

            /* ****************** AUTH ******************* */
            if (action.matches("^auth_(.+)")) {
                final String appName = args.getString(0);
                if (!appAuth.containsKey(appName)) {
                    callbackContext.error("There is no app named " + appName);
                    return true;
                }

                final AuthComponent authComponent = appAuth.get(appName);

                if (action.equals("auth_addAuthStateListener")) {
                    authComponent.setCallbackContext(callbackContext);
                    return true;
                }

                if (action.equals("auth_removeAuthStateListener")) {
                    authComponent.removeCallbackContext();
                    return true;
                }

                if (action.equals("auth_signOut")) {
                    authComponent.signOut(callbackContext);
                    return true;
                }

                if (action.equals("auth_signInAnonymously")) {
                    authComponent.signInAnonymously(callbackContext);
                    return true;
                }

                if (action.equals("auth_signInWithCustomToken")) {
                    authComponent.signInWithCustomToken(callbackContext, args.getString(1));
                    return true;
                }

                if (action.equals("auth_signInWithEmailAndPassword")) {
                    authComponent.signInWithEmailAndPassword(callbackContext, args.getString(1), args.getString(2));
                    return true;
                }

                if (action.equals("auth_signInWithCredential")) {
                    authComponent.signInWithCredential(callbackContext, args.getJSONObject(1));
                    return true;
                }

                if (action.equals("auth_sendPasswordResetEmail")) {
                    authComponent.sendPasswordResetEmail(callbackContext, args.getString(1));
                    return true;
                }

                if (action.equals("auth_verifyPasswordResetCode")) {
                    authComponent.verifyPasswordResetCode(callbackContext, args.getString(1));
                    return true;
                }

                if (action.equals("auth_confirmPasswordReset")) {
                    authComponent.confirmPasswordReset(callbackContext, args.getString(1), args.getString(2));
                    return true;
                }

                if (action.equals("auth_fetchProvidersForEmail")) {
                    authComponent.fetchProvidersForEmail(callbackContext, args.getString(1));
                    return true;
                }

                if (action.equals("auth_checkActionCode")) {
                    authComponent.checkActionCode(callbackContext, args.getString(1));
                    return true;
                }

                if (action.equals("auth_applyActionCode")) {
                    authComponent.applyActionCode(callbackContext, args.getString(1));
                    return true;
                }

                if (action.equals("auth_createUserWithEmailAndPassword")) {
                    authComponent.createUserWithEmailAndPassword(callbackContext, args.getString(1), args.getString(2));
                    return true;
                }

                if (action.equals("auth_user_delete")) {
                    authComponent.user_delete(callbackContext, args.getString(1));
                    return true;
                }

                if (action.equals("auth_user_getToken")) {
                    authComponent.user_getToken(callbackContext, args.getString(1), args.getBoolean(2));
                    return true;
                }

                if (action.equals("auth_user_reload")) {
                    authComponent.user_reload(callbackContext, args.getString(1));
                    return true;
                }

                if (action.equals("auth_user_sendEmailVerification")) {
                    authComponent.user_sendEmailVerification(callbackContext, args.getString(1));
                    return true;
                }

                if (action.equals("auth_user_updateEmail")) {
                    authComponent.user_updateEmail(callbackContext, args.getString(1), args.getString(2));
                    return true;
                }

                if (action.equals("auth_user_updatePassword")) {
                    authComponent.user_updatePassword(callbackContext, args.getString(1), args.getString(2));
                    return true;
                }

                if (action.equals("auth_user_updateProfile")) {
                    authComponent.user_updateProfile(callbackContext, args.getString(1), args.getJSONObject(2));
                    return true;
                }

                if (action.equals("auth_user_link")) {
                    authComponent.user_link(callbackContext, args.getString(1), args.getJSONObject(2));
                    return true;
                }

                if (action.equals("auth_user_unlink")) {
                    authComponent.user_unlink(callbackContext, args.getString(1), args.getString(2));
                    return true;
                }

                if (action.equals("auth_user_reauthenticate")) {
                    authComponent.user_reauthenticate(callbackContext, args.getString(1), args.getJSONObject(2));
                    return true;
                }
            }

            /* ****************** DATABASE ******************* */
            if (action.matches("^database_(.+)")) {
                final String appName = args.getString(0);
                if (!appDatabase.containsKey(appName)) {
                    callbackContext.error("There is no app named " + appName);
                    return true;
                }

                final DatabaseComponent databaseComponent = appDatabase.get(appName);

                if (action.equals("database_goOffline")) {
                    databaseComponent.goOffline(callbackContext);
                    return true;
                }

                if (action.equals("database_goOnline")) {
                    databaseComponent.goOnline(callbackContext);
                    return true;
                }

                if (action.equals("database_setPersistenceEnabled")) {
                    databaseComponent.setPersistenceEnabled(callbackContext, args.getBoolean(1));
                    return true;
                }

                if (action.equals("database_attachListener")) {
                    databaseComponent.attachListener(callbackContext, args.getString(1), args.getBoolean(2),
                            args.getString(3), args.getString(4), args.getJSONObject(5));
                    return true;
                }

                if (action.equals("database_removeListeners")) {
                    databaseComponent.removeListeners(callbackContext, args.getJSONArray(1));
                    return true;
                }

                if (action.equals("database_keepSynced")) {
                    databaseComponent.keepSynced(callbackContext, args.getString(1), args.getBoolean(2));
                    return true;
                }

                if (action.equals("database_set")) {
                    databaseComponent.set(callbackContext, args.getString(1), args.get(2));
                    return true;
                }

                if (action.equals("database_update")) {
                    databaseComponent.update(callbackContext, args.getString(1), args.getJSONObject(2));
                    return true;
                }

                if (action.equals("database_onDisconnect_set")) {
                    databaseComponent.onDisconnect_set(callbackContext, args.getString(1), args.get(2));
                    return true;
                }

                if (action.equals("database_onDisconnect_update")) {
                    databaseComponent.onDisconnect_update(callbackContext, args.getString(1),
                            args.getJSONObject(2));
                    return true;
                }

                if (action.equals("database_onDisconnect_cancel")) {
                    databaseComponent.onDisconnect_cancel(callbackContext, args.getString(1));
                    return true;
                }
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

        } catch (IllegalStateException e) {
            callbackContext.error(e.getMessage());
            return true;
        }
    }

    private void initializeApp(final CallbackContext callbackContext, final String appName, final JSONObject config) {
        if (appAuth.containsKey(appName)) {
            callbackContext.error("App " + appName + " has already been initialized");
            return;
        }

        if (appName.equals("[DEFAULT]")) {
            try {
                FirebaseApp firebaseApp = FirebaseApp.getInstance(appName);
                initializeComponentsWithApp(callbackContext, appName, firebaseApp);
                return;
            } catch (IllegalStateException e) {
                // Default app hasn't been initialized yet. It's ok.
            }
        }

        try {
            final FirebaseOptions.Builder builder = new FirebaseOptions.Builder();
            FirebaseOptions defaultOptions = FirebaseOptions.fromResource(this.context);

            if (defaultOptions == null) {
                Log.i(TAG, "initializeApp: No default options found");
                defaultOptions = new FirebaseOptions.Builder().build();
            }

            String apiKey = getConfigOption(config, "apiKey", defaultOptions.getApiKey());
            if (apiKey != null) {
                builder.setApiKey(apiKey);
            }

            String messagingSenderId = getConfigOption(config, "messagingSenderId", defaultOptions.getGcmSenderId());
            if (messagingSenderId != null) {
                builder.setGcmSenderId(messagingSenderId);
            }

            String storageBucket = getConfigOption(config, "storageBucket", defaultOptions.getStorageBucket());
            if (storageBucket != null) {
                builder.setStorageBucket(storageBucket);
            }

            String databaseURL = getConfigOption(config, "databaseURL", defaultOptions.getDatabaseUrl());
            if (databaseURL != null) {
                builder.setDatabaseUrl(databaseURL);
            }

            String androidAppId = getConfigOption(config, "androidAppId", defaultOptions.getApplicationId());
            if (androidAppId != null) {
                builder.setApplicationId(androidAppId);
            }

            final Firebase self = this;

            // Needs to run on the main (UI) thread
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    FirebaseOptions options = builder.build();
                    Log.i(TAG, "Initializing app " + appName + " with options: " + options.toString());
                    final FirebaseApp firebaseApp = FirebaseApp.initializeApp(context, options, appName);
                    Log.i(TAG, "App " + appName + " initialized");

                    initializeComponentsWithApp(callbackContext, appName, firebaseApp);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error initializing app " + appName);
            callbackContext.error(e.getMessage());
        }

    }

    private void deleteApp(final CallbackContext callbackContext, final String appName) {
        if (!appAuth.containsKey(appName)) {
            callbackContext.error("There is no app named " + appName);
            return;
        }

        try {
            final AuthComponent authComponent = appAuth.get(appName);
            authComponent.reset();
            appAuth.remove(appName);

            final DatabaseComponent databaseComponent = appDatabase.get(appName);
            databaseComponent.reset();
            appDatabase.remove(appName);
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
        }
    }

    private void initializeComponentsWithApp(final CallbackContext callbackContext, final String appName,
                                             final FirebaseApp firebaseApp) {
        final Firebase self = this;
        final Bundle extras = this.cordova.getActivity().getIntent().getExtras();
        this.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                Log.i(TAG, "Initializing plugin components for " + appName);

                if (appName.equals("[DEFAULT]")) {
                    mAnalyticsComponent.initialize(context);
                    MessagingComponent.initialize(extras);
                }

                appAuth.put(appName, new AuthComponent(self, firebaseApp));
                appDatabase.put(appName, new DatabaseComponent(self, firebaseApp));

                callbackContext.success();
            }
        });

    }

    private static String getConfigOption(final JSONObject config, final String key, final String defaultVal) {
        if (config.has(key)) {
            return config.optString(key);
        } else if (defaultVal != null && !defaultVal.equals("")) {
            return defaultVal;
        } else {
            return null;
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

        MessagingComponent.reset();

        for (String appName : appAuth.keySet()) {
            AuthComponent authComponent = appAuth.get(appName);
            authComponent.reset();
        }

        for (String appName : appDatabase.keySet()) {
            DatabaseComponent databaseComponent = appDatabase.get(appName);
            databaseComponent.reset();
        }

        appAuth.clear();
        appDatabase.clear();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Bundle bundle = intent.getExtras();
        if ((bundle != null) && bundle.containsKey("google.message_id")) {
            bundle.putBoolean("tap", true);
            MessagingComponent.sendNotification(bundle);
        }
    }

}
