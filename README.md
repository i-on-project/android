<p align="center">
  <a href="https://www.ionproject.org">
    <img src="https://github.com/i-on-project/android/blob/master/docs/resources/ion_android_logo_smaller.png" alt="ionproject.org" />
  </a>
</p>

[![License](https://img.shields.io/github/license/i-on-project/android)](https://github.com/i-on-project/android/blob/master/LICENSE)
[![GitHub tags](https://img.shields.io/github/v/tag/i-on-project/android)](https://github.com/i-on-project/android/tags)
[![GitHub commits](https://img.shields.io/github/last-commit/i-on-project/android)](https://github.com/i-on-project/android/commits/master)
[![GitHub pull-requests](https://img.shields.io/github/issues-pr/i-on-project/android)](https://github.com/i-on-project/android/pulls/)
[![GitHub issues](https://img.shields.io/github/issues/i-on-project/android)](https://github.com/i-on-project/android/issues/)
[![GitHub contributors](https://img.shields.io/github/contributors/i-on-project/android)](https://github.com/i-on-project/android/graphs/contributors/)

# Android
Repository for the Android i-on component.

I-on Android is a mobile application which presents academic information retrieved from the Web API made available from i-on Core project.

# Testing the application
To test the application one must:

1. Contact the developers via their student emails to obtain the file google-services.json and the authorization token for the read API because they cannot be committed to the repository for security reasons.
  The authorization token can be also obtained from i-on Core's developers.

2. Once google-services.json has been obtained it has to placed in the folder: Project/app

3. The authorization code should be placed in the file Project/app/src/main/java/org/ionproject/android/common/ionwebapi/WebAPIConfigs.kt:
```kotlin 
const val WEB_API_AUTHORIZATION_TOKEN = "ASK_THE_DEVELOPERS" (replace with authorization token)
```

4. Now, if you want to run the application with mocks uncomment this line of code in the file Project/app/src/main/java/org/ionproject/android/common/IonApplication.kt:
  ```kotlin
  // val webAPI = MockIonWebAPI(ionMapper)
  ```
 And comment these lines of code in the same file:
    
  ```kotlin
  val retrofit = Retrofit.Builder()
              .baseUrl("https://host1.dev.ionproject.org")
              .addConverterFactory(ScalarsConverterFactory.create())
              .build()

  val service: IonService = retrofit.create(IonService::class.java)
  val webAPI = IonWebAPI(service, ionMapper)
  ```
5. Finally open the project in the latest version of Android Studio and run the LoadingActivity on an emulator or your phone.

**IMPORTANT:** To run the application the phone or emulator MUST support at least the version 21 of the Android SDK.

