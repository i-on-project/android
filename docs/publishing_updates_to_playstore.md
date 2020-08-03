# Publishing updates to the playstore

First you have to update the app version, that can be done in the build.gradle file located in the "project" folder. 

```gradle
android {
    compileSdkVersion 29
    buildToolsVersion "29.0.2"

    defaultConfig {
        applicationId "org.ionproject.android"
        minSdkVersion 21
        targetSdkVersion 29
        versionCode 3 // INCREASE THIS CODE
        versionName "1.0.2-alpha" // UPDATE THIS STRING
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            ...
```

The ```versionCode``` value **MUST** be increased for each update, this is required so that the play console identifies you new release as an update. The ```versionName``` is the version presented to the user, for each new update it should be changed.

Everytime the local database is updated, its ```version``` **MUST** also be increased, otherwise once the users update the app, room will throw an exception informing the schema version has not been updated. This can be done in the file "org.ionproject.android.common.db.Database":

```kotlin
@Database(
    entities = arrayOf(
        Root::class,
        ...
        Lecture::class,
        Classes::class
    ), version = 1 // INCREASE THIS VALUE,
    exportSchema = true
)
...
```

After all these changes have been made, all you have to do is generate an Android app bundle and sign it. You should first contact the i-on Iniciative owners and obtain the java keystore (jks), the keystore password and the private key password. 
Then you follow this steps:

1. In the Android Studio IDE go to Build -> Generate Signed Bundle/APK...

2. Select Android App Bundle and click next

3. Choose an existing keystore, and use the upload-keystore.jks, the one given by the owners of the iniciative

4. Fill the form, the keystore password, then in the key alias choose the upload key finally write the key password and click next

5. Select release and click finish

The new signed Android app bundle will now build. Once you have the bundle you can then upload it to the Google Play Console as a new release.







