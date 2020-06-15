# Android
Repository for the Android i-on component

# Testing the application
To test the application one must:

1. Contact the developers via their student emails to obtain the file google-services.json because it cannot be committed to the repository for security reasons.

2. Once google-services.json has been obtained it has to placed in the folder: /Project/app

3. Now, if you want to run the application with mocks uncomment this line of code in the file Project/app/src/main/java/org/ionproject/android/common/IonApplication.kt:
  ```kotlin
  // val webAPI = MockIonWebAPI(ionMapper)
  ```
 And comment these lines of code in the same file:
    
  ```kotlin
  val retrofit = Retrofit.Builder()
              .baseUrl("http://10.0.2.2:8080/")
              .addConverterFactory(ScalarsConverterFactory.create())
              .build()

  val service: IonService = retrofit.create(IonService::class.java)
  val webAPI = IonWebAPI(service, ionMapper)
  ```
4. Finally open the project in the latest version of Android Studio and run the LoadingActivity on an emulator or your phone.

**IMPORTANT:** To run the application the phone or emulator MUST support the level 21 Android API 

