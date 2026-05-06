plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.parkingappclient"
    compileSdk = 34 // CHANGED: Use stable version 34 to match targetSdk

    defaultConfig {
        applicationId = "com.example.parkingappclient"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("pr" +
                        "" +
                        "oguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // QR CODE SCANNING
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // NETWORKING (Retrofit and Gson) - CLEANED UP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // ANDROIDX & TESTING
    implementation(libs.appcompat)
    implementation(libs.material)
    // Removed libs.transportation.consumer as its usage is unclear and may be causing instability

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}