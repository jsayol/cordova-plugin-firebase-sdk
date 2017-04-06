package org.apache.cordova.firebase;

import org.apache.cordova.CallbackContext;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Iterator;

public class AnalyticsComponent {
    private static final String TAG = "FirebaseAnalytics";

    private Firebase mFirebase;
    private FirebaseAnalytics mFirebaseAnalytics;

    public AnalyticsComponent(Firebase plugin) {
        Log.i(TAG, "New instance");
        this.mFirebase = plugin;
    }

    public void initialize(Context context) {
        Log.i(TAG, "Initializing");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void logEvent(final CallbackContext callbackContext, final String name, final JSONObject params) throws JSONException {
        final Bundle bundle = new Bundle();
        Iterator iter = params.keys();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            Object value = params.get(key);

            if (value instanceof Integer || value instanceof Double) {
                bundle.putFloat(key, ((Number) value).floatValue());
            } else {
                bundle.putString(key, value.toString());
            }
        }

        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Logging event " + name);
                    mFirebaseAnalytics.logEvent(name, bundle);
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error logging event " + name);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void setScreenName(final CallbackContext callbackContext, final String name) {
        // This must be called on the main thread
        mFirebase.cordova.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting screen name " + name);
                    mFirebaseAnalytics.setCurrentScreen(mFirebase.cordova.getActivity(), name, null);
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error setting screen name " + name);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void setUserId(final CallbackContext callbackContext, final String id) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting user id " + id);
                    mFirebaseAnalytics.setUserId(id);
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error setting user id " + id);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void setUserProperty(final CallbackContext callbackContext, final String name, final String value) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting user property " + name);
                    mFirebaseAnalytics.setUserProperty(name, value);
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error setting user property " + name);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

}
