package org.apache.cordova.firebase;

import org.apache.cordova.CallbackContext;

import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigInfo;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RemoteConfigComponent {
    private static final String TAG = "RemoteConfigComponent";

    private Firebase mFirebase;

    public RemoteConfigComponent(Firebase plugin) {
        Log.i(TAG, "New instance");
        this.mFirebase = plugin;
    }

    public void activateFetched(final CallbackContext callbackContext) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    final boolean activated = FirebaseRemoteConfig.getInstance().activateFetched();
                    callbackContext.success(String.valueOf(activated));
                } catch (Exception e) {
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void fetch(final CallbackContext callbackContext, final long cacheExpirationSeconds) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Fetching");
                    FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
                    Task<Void> task = (cacheExpirationSeconds == 0)
                            ? remoteConfig.fetch()
                            : remoteConfig.fetch(cacheExpirationSeconds);

                    task.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            callbackContext.success();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.e(TAG, "Error fetching", e);
                            callbackContext.error(e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error fetching: " + e.getMessage());
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void getByteArray(final CallbackContext callbackContext, final String key, final String namespace) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Getting byte array " + key);
                    byte[] bytes = namespace == null ? FirebaseRemoteConfig.getInstance().getByteArray(key)
                            : FirebaseRemoteConfig.getInstance().getByteArray(key, namespace);
                    JSONObject object = new JSONObject();
                    object.put("base64", Base64.encodeToString(bytes, Base64.DEFAULT));
                    object.put("array", new JSONArray(bytes));
                    callbackContext.success(object);
                } catch (Exception e) {
                    Log.e(TAG, "Error getting byte array " + key, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void getValue(final CallbackContext callbackContext, final String key, final String namespace) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Getting value " + key);
                    FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
                    FirebaseRemoteConfigValue value = (namespace == null)
                            ? remoteConfig.getValue(key)
                            : remoteConfig.getValue(key, namespace);
                    callbackContext.success(value.asString());
                } catch (Exception e) {
                    Log.e(TAG, "Error getting value " + key, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void getInfo(final CallbackContext callbackContext) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Getting info");
                    FirebaseRemoteConfigInfo remoteConfigInfo = FirebaseRemoteConfig.getInstance().getInfo();

                    JSONObject settings = new JSONObject();
                    settings.put("developerModeEnabled", remoteConfigInfo.getConfigSettings().isDeveloperModeEnabled());

                    JSONObject info = new JSONObject();
                    info.put("configSettings", settings);
                    info.put("fetchTimeMillis", remoteConfigInfo.getFetchTimeMillis());
                    info.put("lastFetchStatus", remoteConfigInfo.getLastFetchStatus());

                    callbackContext.success(info);
                } catch (Exception e) {
                    Log.e(TAG, "Error getting info", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void setConfigSettings(final CallbackContext callbackContext, final JSONObject config) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting config settings");
                    boolean devMode = config.getBoolean("developerModeEnabled");
                    FirebaseRemoteConfigSettings.Builder settings = new FirebaseRemoteConfigSettings.Builder()
                            .setDeveloperModeEnabled(devMode);
                    FirebaseRemoteConfig.getInstance().setConfigSettings(settings.build());
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error setting config settings", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void setDefaults(final CallbackContext callbackContext, final JSONObject defaults, final String namespace) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting defaults");
                    Map<String, Object> defaultsMap = defaultsToMap(defaults);
                    FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
                    if (namespace == null) {
                        remoteConfig.setDefaults(defaultsMap);
                    } else {
                        remoteConfig.setDefaults(defaultsMap, namespace);
                    }
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error setting defaults", e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private static Map<String, Object> defaultsToMap(JSONObject object) throws JSONException {
        final Map<String, Object> map = new HashMap<String, Object>();

        for (Iterator<String> keys = object.keys(); keys.hasNext(); ) {
            String key = keys.next();
            Object value = object.get(key);

            if (value instanceof Integer) {
                // setDefaults() takes Longs
                value = Long.valueOf((Integer) value);
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                if (array.length() == 1 && array.get(0) instanceof String) {
                    //parse byte[] as Base64 String
                    value = Base64.decode(array.getString(0), Base64.DEFAULT);
                } else {
                    //parse byte[] as numeric array
                    byte[] bytes = new byte[array.length()];
                    for (int i = 0; i < array.length(); i++)
                        bytes[i] = (byte) array.getInt(i);
                    value = bytes;
                }
            }

            map.put(key, value);
        }
        return map;
    }
}
