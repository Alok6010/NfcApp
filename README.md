# NFC Card Reader App

This app detects NFC card taps, captures the device's location, and stores the data in Firebase Firestore. It has two tabs:
1. Tap Card Tab: Allows users to tap NFC cards and save the data.
2. History Tab: Displays previously tapped card details.

## Features
- NFC card detection using `NfcAdapter API`.
- Location tracking using `FusedLocationProviderClient`.
- Real-time data storage and retrieval using Firebase Firestore.

## Setup Instructions

### 1. Prerequisites
- Android Studio (latest version).
- An NFC-enabled Android device.
- Firebase project with Firestore enabled.

### 2. Firebase Setup
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project and register your Android app.
3. Download the `google-services.json` file and place it in the `app/` directory.

### 3. Build and Run
1. Open the project in Android Studio.
2. Connect an NFC-enabled Android device or use an emulator with NFC support.
3. Click **Run** to build and install the app on the device.

### 4. Testing
1. Tap an NFC card on the device.
2. Verify that the card data is saved to Firestore.
3. Switch to the **History Tab** to view the saved data.

## Dependencies
- Firebase Firestore: `com.google.firebase:firebase-firestore-ktx:24.7.0`
- Firebase Core: `com.google.firebase:firebase-core:21.1.1`
- Play Services Location: `com.google.android.gms:play-services-location:21.0.1`

## Author
Alok Mishra
