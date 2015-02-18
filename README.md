Ushahidi Android App [![Build Status](https://travis-ci.org/ushahidi/platform-android.svg?branch=master)](https://travis-ci.org/ushahidi/platform-android)
================

Ushahidi is an open source web application for information collection, visualization, and interactive mapping. 
It helps collect data from many sources, including: email, SMS text messaging, Twitter streams, and RSS feeds. 
The platform offers tools to help process that information, categorize it, geo-locate it and publish it on a map.

Ushahidi Android App, is an Android implementation of the [Ushahidi platform][1].

### How To Build The App

This is a Gradle based project. You can build it using Android Studio or from the command line. To 
do so follow the steps below:

1. Install the following software:
       - Android SDK:
         http://developer.android.com/sdk/index.html
       - Gradle:
         http://www.gradle.org/downloads
       - Android Studio - Optional: 
         http://developer.android.com/sdk/installing/studio.html

2. Run the Android SDK Manager by pressing the SDK Manager toolbar button
   in Android Studio or by running the 'android' command in a terminal
   window.

3. In the Android SDK Manager, ensure that the following are installed,
   and are updated to the latest available version:
       - Tools -> Android SDK Platform-tools (rev 20 or above)
       - Tools -> Android SDK Tools (rev 23.0.2 or above)
       - Tools -> Android SDK Build-tools version 20
       - Tools -> Android SDK Build-tools version 19.1
       - Android 4.4 -> SDK Platform (API 19)
       - Android L (API 20, L Preview)
       - Extras -> Android Support Repository
       - Extras -> Android Support Library
       - Extras -> Google Play services
       - Extras -> Google Repository

4. Create a file in the root of the project called local.properties. Enter the path to your Android SDK.
    Eg. `sdk.dir=/opt/android-studio/sdk`

5. Import the project in Android Studio:

    1. Press File > Import Project
    2. Navigate to and choose the settings.gradle file in this project
    3. Press OK

6. Place the following in `app/gradle.properties`:

   ```
   feedbackEmail=<feedback_email_address>
   ```

7. Choose Build > Make Project in Android Studio or run the following
    command in the project root directory:
    
   `./gradlew clean build` Assemble the output(s) of the projects and runs all checks.
   
   `./gradlew test` Execute tests that runs without any connected devices.
   
   `./gradlew spoon` Execute tests that runs on connected devices. This requires a device to be connected.
   
8. To install on your test device:

   ```
    ./gradlew installDebug
   ```

### Release Build

To make a release make sure you have `gradle.properties` in the root of the `app` module with the
following content.

**gradle.properties**
```
releaseKeyStore=<key_store_file>
releaseKeyPassword=<key_password>
releaseKeyStorePassword=<key_store_password>
releaseKeyAlias=key_alias
feedbackEmail=<feedback_email@example.com>
gPlaystoreServiceAccountEmailAddress=<playstore_service_account_email>
gPlaystorePKFile=<google-playstore-pk-file.p12>
```

A typical `gradle.properties` content should look like this:
```
releaseKeyStore=/home/username/.android/debug.keystore
releaseKeyStorePassword=android
releaseKeyAlias=androiddebugkey
releaseKeyPassword=android
feedbackEmail=feedback-email@example.com
gPlaystoreServiceAccountEmailAddress=9323892392132-842jajdkdadummy@developer.gserviceaccount.com
gPlaystorePKFile=/home/username/pdummy-pk-file.p12
```

Then in the project's root directory, issue:

`./release major milestone alpha`

This should build the app, version it, create a tag and push it to the remote repo and publish
to the Google Playstore's alpha track.

[1]: https://github.com/ushahidi/platform