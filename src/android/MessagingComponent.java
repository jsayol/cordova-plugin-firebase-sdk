package org.apache.cordova.firebase;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import android.content.SharedPreferences;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Set;
import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;
import android.support.v4.app.NotificationManagerCompat;

public class MessagingComponent {
    private static final String TAG = "MessagingComponent";

    private Firebase mFirebase;

    public MessagingComponent(Firebase plugin) {
        Log.i(TAG, "New instance");
        this.mFirebase = plugin;
    }

    protected static final String KEY = "badge";

    private static ArrayList<Bundle> notificationStack = null;
    private static CallbackContext notificationCallbackContext;
    private static CallbackContext tokenRefreshCallbackContext;

    public static void initialize(Bundle bundle) {
        Log.i(TAG, "Initializing");
        if ((bundle != null) && (bundle.size() > 1)) {
            if (MessagingComponent.notificationStack == null) {
                MessagingComponent.notificationStack = new ArrayList<Bundle>();
            }
            if (bundle.containsKey("google.message_id")) {
                bundle.putBoolean("tap", true);
                notificationStack.add(bundle);
            }
        }
    }

    public static void reset() {
        Log.i(TAG, "Resetting");
        MessagingComponent.notificationCallbackContext = null;
        MessagingComponent.tokenRefreshCallbackContext = null;
    }

    public static void sendNotification(Bundle bundle) {
        if (!MessagingComponent.hasNotificationsCallback()) {
            MessagingComponent.initialize(bundle);
            return;
        }

        Log.i(TAG, "Sending notification");

        final CallbackContext callbackContext = MessagingComponent.notificationCallbackContext;
        if (callbackContext != null && bundle != null) {
            JSONObject json = new JSONObject();
            Set<String> keys = bundle.keySet();
            for (String key : keys) {
                try {
                    json.put(key, bundle.get(key));
                } catch (JSONException e) {
                    Log.i(TAG, "Error sending notification: " + e.getMessage());
                    callbackContext.error(e.getMessage());
                    return;
                }
            }

            PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, json);
            pluginresult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginresult);
        }
    }

    public static void sendToken(String token) {
        if (MessagingComponent.tokenRefreshCallbackContext == null) {
            return;
        }

        Log.i(TAG, "Sending token " + token);

        final CallbackContext callbackContext = MessagingComponent.tokenRefreshCallbackContext;
        if (callbackContext != null && token != null) {
            PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, token);
            pluginresult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginresult);
        }
    }

    public static boolean hasNotificationsCallback() {
        return MessagingComponent.notificationCallbackContext != null;
    }

    public void onNotificationOpen(final CallbackContext callbackContext) {
        Log.i(TAG, "Setting onNotificationOpen callback");

        MessagingComponent.notificationCallbackContext = callbackContext;
        if (MessagingComponent.notificationStack != null) {
            for (Bundle bundle : MessagingComponent.notificationStack) {
                MessagingComponent.sendNotification(bundle);
            }
            MessagingComponent.notificationStack.clear();
        }
    }

    public void onTokenRefresh(final CallbackContext callbackContext) {
        MessagingComponent.tokenRefreshCallbackContext = callbackContext;

        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting onTokenRefresh callback");
                    String currentToken = FirebaseInstanceId.getInstance().getToken();

                    if (currentToken != null) {
                        MessagingComponent.sendToken(currentToken);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error setting onTokenRefresh callback");
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void getToken(final CallbackContext callbackContext) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Getting token");
                    String token = FirebaseInstanceId.getInstance().getToken();
                    callbackContext.success(token);
                } catch (Exception e) {
                    Log.e(TAG, "Error getting token");
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void hasPermission(final CallbackContext callbackContext) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Checking permission");
                    Context context = mFirebase.cordova.getActivity();
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                    boolean areNotificationsEnabled = notificationManagerCompat.areNotificationsEnabled();
                    JSONObject object = new JSONObject();
                    object.put("isEnabled", areNotificationsEnabled);
                    callbackContext.success(object);
                } catch (Exception e) {
                    Log.e(TAG, "Error checking permission");
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void setBadgeNumber(final CallbackContext callbackContext, final int number) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting badge number " + number);
                    Context context = mFirebase.cordova.getActivity();
                    SharedPreferences.Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
                    editor.putInt(KEY, number);
                    editor.apply();
                    ShortcutBadger.applyCount(context, number);
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error setting badge number " + number);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void getBadgeNumber(final CallbackContext callbackContext) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Getting badge number");
                    Context context = mFirebase.cordova.getActivity();
                    SharedPreferences settings = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
                    int number = settings.getInt(KEY, 0);
                    callbackContext.success(number);
                } catch (Exception e) {
                    Log.e(TAG, "Error getting badge number");
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void subscribe(final CallbackContext callbackContext, final String topic) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Subscribing to topic " + topic);
                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error subscribing to topic " + topic);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void unsubscribe(final CallbackContext callbackContext, final String topic) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Unsubscribing from topic " + topic);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error unsubscribing from topic " + topic);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

}
