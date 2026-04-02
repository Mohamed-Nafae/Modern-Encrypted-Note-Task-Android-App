# 🛡️ CipherNote 
**Modern Encrypted Note & Task Manager for Android**

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-blue?logo=kotlin)
![Android](https://img.shields.io/badge/Android-Native-4CAF50?logo=android)
![Status](https://img.shields.io/badge/Status-Active-success)

Welcome to **CipherNote**, the ultimate Modern Encrypted Note & Task Manager designed to keep your private data completely safe. 

In a world where privacy is increasingly compromised, CipherNote provides a secure sanctuary for your personal thoughts, important tasks, and sensitive information. Built with a beautiful, modern interface, it combines powerful productivity tools with industry-standard, military-grade encryption. Your data stays on your device, and only you hold the key.

<a href="YOUR_PLAY_STORE_LINK_HERE">
  <img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="70"/>
</a>


## 🛠️ Tech Stack

CipherNote is built using modern Android development practices and libraries to ensure a robust, secure, and maintainable codebase:

*   **Language:** 100% [Kotlin](https://kotlinlang.org/)
*   **Architecture:** MVVM (Model-View-ViewModel) with Clean Architecture principles.
*   **Local Data Storage:** [Room Persistence Library](https://developer.android.com/training/data-storage/room) for robust local storage.
*   **Asynchronous Programming:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) &[Flow](https://kotlinlang.org/docs/flow.html) for smooth, non-blocking UI interactions.
*   **Security & Encryption:**
    *   [Android Keystore System](https://developer.android.com/training/articles/keystore) for cryptographic key management.
    *   [Biometric API](https://developer.android.com/training/sign-in/biometric-auth) for Fingerprint/Face ID authentication.
*   **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) - Modern, declarative UI.
*   **UI & Design:** Material Design Components, Custom Views, and smooth animations supporting both Light and Dark Themes.
*   **Build System:** Gradle with Kotlin DSL (`build.gradle.kts`).


## ✨ Features

### 🛡️ Uncompromising Security
*   **Biometric Vault:** Lock your most sensitive notes behind your device's native fingerprint scanner or Face ID. Your privacy is our absolute priority.
*   **End-to-End Local Encryption:** Secure your notes individually. Encrypt or decrypt items on the fly using our secure underlying architecture.
*   **Offline First & Zero Tracking:** CipherNote requires no internet connection to function. We do not sync to cloud servers, meaning your data can never be breached from the outside.

### 📝 Rich & Intuitive Note-Taking
*   **Flexible Formatting:** Give your notes structure with dedicated Titles, Subtitles, and expansive body text areas.
*   **Color Coding:** Organize your thoughts visually! Assign custom colors (Blue, Red, Green, Yellow, and more) to quickly categorize and find your notes at a glance.
*   **Media Attachments:** Need more context? Easily attach images directly from your gallery into your secure notes.
*   **Hybrid Task Integration:** Don't just write; take action. Instantly add task checklists inside your notes to keep your daily to-dos organized.

### ⚡ Modern & Effortless Organization
*   **Smart Selection Mechanism:** Managing your notes has never been easier. Simply tap a note to select it (highlighted with a sleek yellow border) to use the quick action bar. From there, you can Edit, Delete, Encrypt, or Reorder with a single tap.
*   **Quick Search:** Find exactly what you are looking for instantly using the clean, accessible search bar on the home screen.
*   **Minimalist UI:** Enjoy a distraction-free environment. CipherNote is designed with modern aesthetics, smooth transitions, and an intuitive user experience with support for both **Light and Dark modes**.


## 📸 Screenshots

| Onboarding & Security | Home Screen (Light & Dark) | Note Creation & Tasks |
|:---:|:---:|:---:|
| <img src="screenshots/onboarding.png" width="250"/> | <img src="screenshots/home_light.png" width="250"/> | <img src="screenshots/note_edit_light.png" width="250"/> |
| <img src="screenshots/biometric.png" width="250"/> | <img src="screenshots/home_dark.png" width="250"/> | <img src="screenshots/note_edit_dark.png" width="250"/> |


## 🚀 Getting Started

### Prerequisites
*   Android Studio (Latest version recommended)
*   Minimum SDK: 24 (Adjust if different)
*   Kotlin plugin enabled

### Installation
1. Clone this repository:
   ```bash
   git clone https://github.com/Mohamed-Nafae/Modern-Encrypted-Note-Task-Android-App.git
   ```
2. Open the project in Android Studio.
3. Allow Gradle to sync and download the necessary dependencies.
4. Build and run the application on an Android emulator or a physical device.

## 🤝 Contributing & Feedback
Take Back Control of Your Privacy. Whether you are journaling, storing passwords, planning a project, or just writing down your daily grocery list, CipherNote ensures your digital life remains completely private and beautifully organized.

We are always looking to improve! If you have any feedback, suggestions, or encounter any bugs in the app, please feel free to:
* Open an Issue to report bugs or request features.
* Submit a Pull Request if you want to contribute to the code.

## 🌟 A Quick Note
I hope you find this project useful! Feel free to grab whatever you want from this repository, whether it's copying snippets of code,
adopting the architecture, or using the entire app as a template for your own future projects.
I would be really thankful and happy if this helps you in your development journey!

## 📄 License
This project is open-source and available under the [MIT License](https://opensource.org/license/MIT).
