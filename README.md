# Android
Repository for the Android i-on component

# Testing the application
To test the application one must:

1. Contact the developers via their student emails to obtain the file google-services.json and the authorization token for the read API because they cannot be committed to the repository for security reasons.

2. Once google-services.json has been obtained it has to placed in the folder: Project/app

3. The authorization code should be placed in the file Project/app/src/main/java/org/ionproject/android/common/ionwebapi/IonService.kt:
```kotlin 
private const val AUTHORIZATION_TOKEN = "ASK_THE_DEVELOPERS" (replace with authorization token)
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

**IMPORTANT:** To run the application the phone or emulator MUST support the level 21 Android API 

