Google Authentication With Firebase (Android-Java):

Step 1 - Setup Firebase

- Before you can use Googles Firebase authentication provider, you must first setup and 
  add a Firebase project to your Android studio app. Follow the link below to get started 
  with firebase: https://firebase.google.com/docs/android/setup#groovy

Step 2 - Setup Google Authentication 

- After adding your firebase project to your app, incluse the necessary dependencies necessary
  to use Googles authentication service, setup your apps SHA fingerprint, and enable Google as 
  a sign-in method in the Firebase console. For more details on this, follow the link below;
  https://firebase.google.com/docs/auth/android/google-signin
- Note: to get your Android studio app's SHA certificate fingerprints, you can enter the 
  following command in the IDEs terminal; ./gradlew signingReport.

- Also ensure to remove the default_web_client_id from the res/values/strings.xml, and use the 
  one that comes with the google_services.json file, which is the web client ID necessary for 
  the authentication process.

Step 3 - Run the Project

- Once your done with steps 1 and 2, run the application on an installed emulator (with Google 
  set up) or your pysical device in android studio, and test it for yourself.

For consultancy or assistance in going through the whole process, feel free to email 
lusekelomwalweni@gmail.com,  and will get back to you as soon as possible.
 
Hope this helps with your project! 
