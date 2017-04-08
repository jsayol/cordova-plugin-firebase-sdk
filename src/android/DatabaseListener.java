package org.apache.cordova.firebase;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;
import org.json.JSONObject;

class DatabaseListener {
    private static final String TAG = "DatabaseListener";

    private DatabaseComponent mDatabaseComponent;
    private CallbackContext callbackContext;
    private String listenerID;
    private boolean once;
    private String eventType;
    private JSONObject queryOptions;
    private Query mQuery;
    private ValueEventListener mValueEventListener = null;
    private ChildEventListener mChildEventListener = null;

    DatabaseListener(final DatabaseComponent databaseComponent,
                     final CallbackContext callbackContext,
                     final String listenerID,
                     final boolean once,
                     final String path,
                     final String eventType,
                     final JSONObject queryOptions) {
        Log.i(TAG, "New instance");

        this.mDatabaseComponent = databaseComponent;
        this.callbackContext = callbackContext;
        this.listenerID = listenerID;
        this.once = once;
        this.eventType = eventType;
        this.queryOptions = queryOptions;

        mDatabaseComponent.mFirebase.cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                try {
                    buildQuery(path);

                    if (eventType.equals("value")) {
                        buildValueListener();
                        mQuery.addValueEventListener(mValueEventListener);
                    } else if (eventType.matches("^child_(added|changed|moved|removed)$")) {
                        buildChildListener();
                        mQuery.addChildEventListener(mChildEventListener);
                    } else {
                        callbackContext.error("Unknown event type " + eventType);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error creating listener: " + e.getMessage());
                    callbackContext.error(e.getMessage());
                }
            }
        });
    }

    private void buildQuery(final String path) {
        try {
            final DatabaseReference dbReference = mDatabaseComponent.mFirebaseDatabase.getReference(path);

            if (queryOptions.has("orderByValue")) {
                mQuery = dbReference.orderByValue();
            } else if (queryOptions.has("orderByPriority")) {
                mQuery = dbReference.orderByPriority();
            } else if (queryOptions.has("orderByChild")) {
                mQuery = dbReference.orderByChild(queryOptions.getString("orderByChild"));
            } else {
                mQuery = dbReference.orderByKey();
            }

            if (queryOptions.has("limitToFirst")) {
                mQuery = mQuery.limitToFirst(queryOptions.getInt("limitToFirst"));
            } else if (queryOptions.has("limitToLast")) {
                mQuery = mQuery.limitToLast(queryOptions.getInt("limitToLast"));
            }

            if (queryOptions.has("equalTo")) {
                final JSONObject equalTo = queryOptions.getJSONObject("equalTo");
                final String valueType = equalTo.getString("valueType");

                if (valueType.equals("string")) {
                    final String value = equalTo.getString("value");
                    if (equalTo.has("key")) {
                        mQuery = mQuery.equalTo(value, equalTo.getString("key"));
                    } else {
                        mQuery = mQuery.equalTo(value);
                    }
                } else if (valueType.equals("number")) {
                    final double value = equalTo.getDouble("value");
                    if (equalTo.has("key")) {
                        mQuery = mQuery.equalTo(value, equalTo.getString("key"));
                    } else {
                        mQuery = mQuery.equalTo(value);
                    }
                } else if (valueType.equals("boolean")) {
                    final boolean value = equalTo.getBoolean("value");
                    if (equalTo.has("key")) {
                        mQuery = mQuery.equalTo(value, equalTo.getString("key"));
                    } else {
                        mQuery = mQuery.equalTo(value);
                    }
                } else if (valueType.equals("null")) {
                    if (equalTo.has("key")) {
                        mQuery = mQuery.equalTo(null, equalTo.getString("key"));
                    } else {
                        mQuery = mQuery.equalTo(null);
                    }
                }
            } else {
                if (queryOptions.has("startAt")) {
                    final JSONObject startAt = queryOptions.getJSONObject("startAt");
                    final String valueType = startAt.getString("valueType");

                    if (valueType.equals("string")) {
                        final String value = startAt.getString("value");
                        if (startAt.has("key")) {
                            mQuery = mQuery.startAt(value, startAt.getString("key"));
                        } else {
                            mQuery = mQuery.startAt(value);
                        }
                    } else if (valueType.equals("number")) {
                        final double value = startAt.getDouble("value");
                        if (startAt.has("key")) {
                            mQuery = mQuery.startAt(value, startAt.getString("key"));
                        } else {
                            mQuery = mQuery.startAt(value);
                        }
                    } else if (valueType.equals("boolean")) {
                        final boolean value = startAt.getBoolean("value");
                        if (startAt.has("key")) {
                            mQuery = mQuery.startAt(value, startAt.getString("key"));
                        } else {
                            mQuery = mQuery.startAt(value);
                        }
                    } else if (valueType.equals("null")) {
                        if (startAt.has("key")) {
                            mQuery = mQuery.startAt(null, startAt.getString("key"));
                        } else {
                            mQuery = mQuery.startAt(null);
                        }
                    }
                }

                if (queryOptions.has("endAt")) {
                    final JSONObject endAt = queryOptions.getJSONObject("endAt");
                    final String valueType = endAt.getString("valueType");

                    if (valueType.equals("string")) {
                        final String value = endAt.getString("value");
                        if (endAt.has("key")) {
                            mQuery = mQuery.endAt(value, endAt.getString("key"));
                        } else {
                            mQuery = mQuery.endAt(value);
                        }
                    } else if (valueType.equals("number")) {
                        final double value = endAt.getDouble("value");
                        if (endAt.has("key")) {
                            mQuery = mQuery.endAt(value, endAt.getString("key"));
                        } else {
                            mQuery = mQuery.endAt(value);
                        }
                    } else if (valueType.equals("boolean")) {
                        final boolean value = endAt.getBoolean("value");
                        if (endAt.has("key")) {
                            mQuery = mQuery.endAt(value, endAt.getString("key"));
                        } else {
                            mQuery = mQuery.endAt(value);
                        }
                    } else if (valueType.equals("null")) {
                        if (endAt.has("key")) {
                            mQuery = mQuery.endAt(null, endAt.getString("key"));
                        } else {
                            mQuery = mQuery.endAt(null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error building query");
            callbackContext.error(e.getMessage());
        }

    }

    private void buildValueListener() {
        try {
            Log.i(TAG, "Building value listener");
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    processJSONResult(dataSnapshot, null, false);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i(TAG, "ValueEventListener:onCancelled" + databaseError.getMessage());
                    mDatabaseComponent.removeListener(listenerID);
                    mValueEventListener = null;
                    callbackContext.error(databaseError.getMessage());
                }
            };
        } catch (Exception e) {
            Log.e(TAG, "Error building query");
            callbackContext.error(e.getMessage());
        }
    }

    private void buildChildListener() {
        try {
            Log.i(TAG, "Building child listener");
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    if (eventType.equals("child_added")) {
                        processJSONResult(dataSnapshot, previousChildName, true);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    if (eventType.equals("child_changed")) {
                        processJSONResult(dataSnapshot, previousChildName, true);
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    if (eventType.equals("child_moved")) {
                        processJSONResult(dataSnapshot, previousChildName, true);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    if (eventType.equals("child_removed")) {
                        processJSONResult(dataSnapshot, null, true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.i(TAG, "ChildEventListener:onCancelled: " + databaseError.getMessage());
                    mDatabaseComponent.removeListener(listenerID);
                    mChildEventListener = null;
                    callbackContext.error(databaseError.getMessage());
                }
            };
        } catch (Exception e) {
            Log.e(TAG, "Error building query: " + e.getMessage());
            callbackContext.error(e.getMessage());
        }
    }

    private void processJSONResult(DataSnapshot dataSnapshot, String previousChildName, boolean includeKey) {
        try {
            Object value = DatabaseComponent.toJSONCompatibleObject(dataSnapshot.getValue());

            JSONObject result = new JSONObject();
            result.put("value", value);
            result.put("prevChildKey", previousChildName);

            if (includeKey) {
                result.put("key", dataSnapshot.getKey());
            }

            PluginResult pluginresult = new PluginResult(PluginResult.Status.OK, result);
            pluginresult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginresult);
        } catch (Exception e) {
            Log.e(TAG, "getJSONResult: JSONException " + e.getMessage());
        }

        if (once) {
            removeListener();
            mDatabaseComponent.removeListener(listenerID);
        }
    }

    void removeListener() {
        if (mValueEventListener != null) {
            Log.i(TAG, "Removing value event listener");
            mQuery.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }

        if (mChildEventListener != null) {
            Log.i(TAG, "Removing child event listener");
            mQuery.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

}