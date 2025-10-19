plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // id("org.jetbrains.kotlin.kapt")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")





}

android {
    namespace = "com.example.csc202assignment"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.csc202assignment"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.fragment:fragment-ktx:1.8.8")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.3")

    // Lifecycle & ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Room (Ensure versions match)
    val room_version = "2.6.1" // Using 2.6.1 as it's the version for your compiler
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // UI
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    // Google Play Services
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-maps:19.2.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

}