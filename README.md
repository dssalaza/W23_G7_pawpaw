# PawPaw app
An Android mobile booking application that connects busy pet owners with mobile pet grooming professionals. 

## Getting Started
PawPaw essentially adopts SQLite for CRUD operations throughout the application. In addtion, PawPaw integrates with third parties such as [GoogleAPI](https://developers.google.com/identity/sign-in/android/start-integrating), [firebase](https://firebase.google.com/) and [Stripe](https://stripe.com/en-ca). In order to run the project properly you'll need to follow the next steps:

### Prerequisites

First, in order to use Google Sign in (3rd Party), we have to install and run a emulator/AVD (Android Virtual Device) with Google Play that includes Google Play services and the Google Play Store such as Pixel 4 or 6.

To adopt google sign in button and the service, we have ensure Google's Maven repository is included:
app/Gradle Scripts/settings.gradle/
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
} 
```
app/Gradle Scripts/build.gradle file, declar Google Play services as a dependency:
```
implementation 'com.github.TutorialsAndroid:GButton:v1.0.19' 
implementation 'com.google.android.gms:play-services-auth:20.4.0'  
```
To configure a Google API Console project, we need Package name and SHA-1 from gradle and Create.


PawPaw communicates with firebase, therefore it needs a `google-services.json` file. For security reasons this file is not versioned so please ask for this file to the contributors of the project and place it in the `app/` directory of the project.
```
app/google-services.json
```

In order to use google maps to get the current location of the user, we need a MAPS_API_KEY. Please ask for this to the contributors of the project and put the value in local.properties file in your project.

```
MAPS_API_KEY = "YOUR_API_KEY"
```

In order to make payments the application needs to communicate with Stripe. In order to do so it needs a `STRIPE_SECRET_KEY` and a `STRIPE_PUBLISHABLE_KEY`. Please ask for this file to the contributors of the project and replace it in the `CheckOutActivity.java`

```
On CheckOutActivity.java add the respective values to each secret key
    
final String STRIPE_SECRET_KEY = "<STRIPE_SECRET_KEY>";
final String STRIPE_PUBLISHABLE_KEY = "<STRIPE_PUBLISHABLE_KEY>";
```
