# Photo Manager App

An Android app for managing photos with advanced functionalities such as camera access, photo searching, sorting, and detailed photo information. Built using Jetpack Compose, Kotlin, and modern Android development practices.

## Features

- Capture photos using the device camera.
- Display a list of photos with search and sorting functionality.
- Full-screen view for selected photos.
- Sort photos by name or date (ascending or descending).
- Search photos by name or creation date.

## Tech Stack

- **Kotlin** - Programming language
- **Jetpack Compose** - UI toolkit for building native UI
- **ViewModel** - Lifecycle-aware component for UI-related data
- **StateFlow** - Asynchronous data stream
- **MockK** - Mocking library for Kotlin

## Getting Started

### Prerequisites

- Android Studio Bumblebee or later
- Gradle 7.0 or later
- Minimum Android SDK version 26

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/yourusername/photo-manager-app.git
    cd photo-manager-app
    ```

2. Open the project in Android Studio.

3. Build the project:

    - Open `Build` > `Make Project` or press `Ctrl+F9`.

### Usage

- Run the app on an Android device or emulator.
- Capture photos using the floating action button.
- Search for photos using the search bar at the top.
- Sort photos by name or date using the dropdown menu.
- Tap on any photo thumbnail to view it in full screen.

## Project Structure

- **MainActivity.kt** - Entry point of the app, initializes the app's composables.
- **PhotoManagerApp.kt** - Sets up the navigation and main app layout.
- **PhotoListScreen.kt** - Displays the list of photos, with search and sorting functionalities.
- **FullScreenPhotoScreen.kt** - Full-screen view for selected photos.
- **ViewModel.kt** - Manages app state and business logic.
- **Utils.kt** - Utility functions, such as saving images.
