# Cordova plugin for Firebase
### Note: only Android support at the moment

## What's implemented
* **Database** (everything except transactions)
* **Authentication**
    * Anonymous
    * Email and password
    * *Social providers coming soon (Google, Facebook, etc)*
* Cloud Messaging
* Remote Config
* Analytics
* Crash Reporting

## API
Documentation coming soon. For now you can browse the TypeScript definition files in the [www](./www) directory
to get an idea of how to do things. I've tried to keep the API as close to the JavaScript SDK as possible.

This should get you started:

```js
const config = {
    apiKey: "...",
    authDomain: "...",
    databaseURL: "...",
    projectId: "...",
    storageBucket: "...",
    messagingSenderId: "..."
};

Firebase.initializeApp(config).then(() => {
    // The default app has been initialized correctly

    // Get notified when a user signs in or out
    Firebase.auth().onAuthStateChanged(user => {
        if (user === null) {
            console.log("The user has signed out");
        } else {
            console.log(`User with uid ${user.uid} and name ${user.displayName} has signed in`);
        }
    });

    // Sign in with email and password
    Firebase.auth()
        .signInWithEmailAndPassword(myEmail, myPassword)
        .then(user => console.log(`User ${user.uid} signed in correctly`))
        .catch(error => console.error("Oops, signing in failed:", error));

    // Enable persistence to disk for the database (YAY! Offline support!)
    Firebase.database().setPersistenceEnabled(true);

    // Write to the database
    Firebase.database()
        .ref("posts")
        .push({from: "Josep", text: "Hey there"})
        .then(() => console.log("Post published!"));

    // Read from the database
    Firebase.database().ref("posts").on("value", snapshot => {
        console.log(`There are ${snapshot.numChildren()} posts`);
    });

    Firebase.database().ref("posts").on("child_added", snapshot => {
        let value = snapshot.val();
        console.log(`Post with key ${snapshot.key} from ${value.from} says: ${value.text}`);
    });

    Firebase.database()
        .ref("posts")
        .orderByChild("from")
        .equalTo("Josep")
        .limitToLast(1)
        .once("child_added", snapshot => {
            console.log("Last post from Josep:", snapshot.val());
        });

    // Receive FCM notifications
    Firebase.messaging().onNotificationOpen(notification => {
        console.log("Hey! New notification:", notification)
    });

});
```
## Acknowledgements
Part of the Android code for Analytics, Messaging, and Remote Config has been forked from the [cordova-plugin-firebase](https://github.com/arnesson/cordova-plugin-firebase) plugin developed by Robert Arnesson.