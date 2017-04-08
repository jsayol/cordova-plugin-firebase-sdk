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
Documentation coming soon. For now you can browse the TypeScript source files in the [scripts/src](./scripts/src) directory
to get an idea of how to do things. I've tried to keep the API as close to the JavaScript SDK as possible.

To get you started, download the `google-services.json` file from the Firebase console and put it in the root
of your Cordova app. After that, you're ready to start using the plugin:

```js
// Enable persistence to disk for the database (Yay! Offline support!)
firebase.database().setPersistenceEnabled(true);

// Write to the database
firebase.database()
    .ref("posts")
    .push({from: "Josep", text: "Hey there"})
    .then(() => console.log("Post published!"));

// Read from the database
firebase.database().ref("posts").on("value", snapshot => {
    console.log(`There are ${snapshot.numChildren()} posts`);
});

firebase.database().ref("posts").on("child_added", snapshot => {
    let value = snapshot.val();
    console.log(`Post with key ${snapshot.key} from ${value.from} says: ${value.text}`);
});

firebase.database()
    .ref("posts")
    .orderByChild("from")
    .equalTo("Josep")
    .limitToLast(1)
    .once("child_added", snapshot => {
        console.log("Last post from Josep:", snapshot.val());
    });

// Get notified when a user signs in or out
firebase.auth().onAuthStateChanged(user => {
    if (user === null) {
        console.log("The user has signed out");
    } else {
        console.log(`User with uid ${user.uid} and name ${user.displayName} has signed in`);
    }
});

// Sign in with email and password
firebase.auth()
    .signInWithEmailAndPassword(myEmail, myPassword)
    .then(user => console.log(`User ${user.uid} signed in correctly`))
    .catch(error => console.error("Oops, signing in failed:", error));

// Receive FCM notifications
firebase.messaging().onNotificationOpen(notification => {
    console.log("Hey! New notification:", notification)
});

```
## Acknowledgements
Part of the Android code for Analytics, Messaging, and Remote Config has been forked from the [cordova-plugin-firebase](https://github.com/arnesson/cordova-plugin-firebase) plugin developed by Robert Arnesson.