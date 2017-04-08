package org.apache.cordova.firebase;

import org.apache.cordova.CallbackContext;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.OnDisconnect;

import java.util.*;

public class DatabaseComponent {
    private static final String TAG = "DatabaseComponent";

    Firebase mFirebase;
    FirebaseDatabase mFirebaseDatabase;
    private HashMap<String, DatabaseListener> listeners;

    public DatabaseComponent(final Firebase plugin, final FirebaseApp firebaseApp) {
        Log.i(TAG, "New instance, initializing");
        mFirebase = plugin;
        listeners = new HashMap<String, DatabaseListener>();
        mFirebaseDatabase = FirebaseDatabase.getInstance(firebaseApp);
    }

    public void reset() {
        Log.i(TAG, "Resetting");
        for (String listenerID: listeners.keySet()) {
            DatabaseListener listener = listeners.get(listenerID);
            listener.removeListener();
        }
        listeners.clear();
    }

    public void goOffline(final CallbackContext callbackContext) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Going offline");
                    mFirebaseDatabase.goOffline();
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error going offline");
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void goOnline(final CallbackContext callbackContext) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Going online");
                    mFirebaseDatabase.goOnline();
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error going online");
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void setPersistenceEnabled(final CallbackContext callbackContext, final boolean isEnabled) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting persistence enabled to " + isEnabled);
                    mFirebaseDatabase.setPersistenceEnabled(isEnabled);
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error setting persistence enabled to " + isEnabled);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void attachListener(final CallbackContext callbackContext,
                               final String listenerID,
                               final boolean once,
                               final String path,
                               final String eventType,
                               final JSONObject queryOptions) {
        final DatabaseComponent self = this;
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Attaching " + eventType + " listener on path " + path);
                    DatabaseListener listener = new DatabaseListener(
                            self,
                            callbackContext,
                            listenerID,
                            once,
                            path,
                            eventType,
                            queryOptions
                    );
                    listeners.put(listenerID, listener);
                } catch (Exception e) {
                    Log.e(TAG, "Error attaching " + eventType + " listener on path " + path);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    void removeListener(final String listenerID) {
        listeners.remove(listenerID);
    }

    public void removeListeners(final CallbackContext callbackContext, final JSONArray listenerIDs) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    int size = listenerIDs.length();
                    Log.i(TAG, "Removing " + size + "listeners");
                    for (int i = 0; i < size; i++) {
                        String listenerID = listenerIDs.optString(i);
                        if (listenerID != null) {
                            DatabaseListener listener = listeners.get(listenerID);
                            listener.removeListener();
                            removeListener(listenerID);
                        }
                    }
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error removing listeners");
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void keepSynced(final CallbackContext callbackContext, final String path, final boolean keepSynced) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting keepSynced to " + keepSynced + " on  path " + path);
                    mFirebaseDatabase.getReference(path).keepSynced(keepSynced);
                    callbackContext.success();
                } catch (Exception e) {
                    Log.e(TAG, "Error setting keepSynced to " + keepSynced + " on  path " + path);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void set(final CallbackContext callbackContext, final String path, final Object value) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting value on path " + path);
                    Object valueObject = toDatabaseObject(value);
                    DatabaseReference dbReference = mFirebaseDatabase.getReference(path);
                    dbReference.setValue(valueObject, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {
                                Log.e(TAG, "Error setting value on path " + path, error.toException());
                                callbackContext.error(error.getMessage());
                            } else {
                                callbackContext.success();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error setting value on path " + path, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void update(final CallbackContext callbackContext, final String path, final JSONObject value) {
        if (!(value instanceof JSONObject)) {
            Log.e(TAG, "Error updating children on path " + path);
            callbackContext.error("Update value must be an object with child paths as keys");
            return;
        }

        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Updating children on path " + path);
                    Map<String, Object> valueObject = (Map<String, Object>) toDatabaseObject(value);
                    DatabaseReference dbReference = mFirebaseDatabase.getReference(path);
                    dbReference.updateChildren(valueObject, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {
                                Log.e(TAG, "Error updating children on path " + path, error.toException());
                                callbackContext.error(error.getMessage());
                            } else {
                                callbackContext.success();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error updating children on path " + path);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void onDisconnect_set(final CallbackContext callbackContext, final String path, final Object value) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting onDisconnect value on path " + path);
                    Object valueObject = toDatabaseObject(value);
                    DatabaseReference dbReference = mFirebaseDatabase.getReference(path);
                    OnDisconnect onDisconnect = dbReference.onDisconnect();
                    onDisconnect.setValue(valueObject, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {
                                Log.e(TAG, "Error setting onDisconnect value on path " + path, error.toException());
                                callbackContext.error(error.getMessage());
                            } else {
                                callbackContext.success();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error setting onDisconnect value on path " + path, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void onDisconnect_update(final CallbackContext callbackContext, final String path, final Object value) {
        if (!(value instanceof JSONObject)) {
            Log.e(TAG, "Error setting onDisconnect update " + path);
            callbackContext.error("Update value for onDisconnect must be an object with child paths as keys");
            return;
        }

        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Setting onDisconnect update on path " + path);
                    Map<String, Object> valueObject = (Map<String, Object>) toDatabaseObject(value);
                    DatabaseReference dbReference = mFirebaseDatabase.getReference(path);
                    OnDisconnect onDisconnect = dbReference.onDisconnect();
                    onDisconnect.updateChildren(valueObject, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {
                                Log.e(TAG, "Error setting onDisconnect update on path " + path, error.toException());
                                callbackContext.error(error.getMessage());
                            } else {
                                callbackContext.success();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error setting onDisconnect update on path " + path, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public void onDisconnect_cancel(final CallbackContext callbackContext, final String path) {
        mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    Log.i(TAG, "Cancelling onDisconnect on path " + path);
                    DatabaseReference dbReference = mFirebaseDatabase.getReference(path);
                    OnDisconnect onDisconnect = dbReference.onDisconnect();
                    onDisconnect.cancel(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error, DatabaseReference ref) {
                            if (error != null) {
                                Log.e(TAG, "Error cancelling onDisconnect value on path " + path, error.toException());
                                callbackContext.error(error.getMessage());
                            } else {
                                callbackContext.success();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error cancelling onDisconnect on path " + path, e);
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    public static Object toDatabaseObject(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return Long.valueOf((Integer) value);
        }

        if (value instanceof JSONArray) {
            JSONArray array = (JSONArray) value;
            List<Object> list = new ArrayList<Object>();

            for (int i = 0, len = array.length(); i < len; i++) {
                list.add(toDatabaseObject(array.opt(i)));
            }

            return list;
        }

        if (value instanceof JSONObject) {
            Map<String, Object> update = new HashMap<String, Object>();
            JSONObject object = (JSONObject) value;
            Iterator<String> keysIterator = object.keys();

            while (keysIterator.hasNext()) {
                String childKey = keysIterator.next();
                Object childValue = object.opt(childKey);
                update.put(childKey, toDatabaseObject(childValue));
            }

            return update;
        }

        return value;
    }

    public static Object toJSONCompatibleObject(Object value) {
        if (value instanceof List) {
            return new JSONArray((List) value);
        }

        if (value instanceof Map) {
            return new JSONObject((Map) value);
        }

        return value;
    }
}

