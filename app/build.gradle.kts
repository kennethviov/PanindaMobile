plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // 🔸 Remove Firebase appdistribution here if unused, or use only in project-level build.gradle
}

android {
    namespace = "dev.komsay.panindamobile"
    compileSdk = 34 // ✅ 36 is too new (not stable yet). Use 34 for compatibility.

    defaultConfig {
        applicationId = "dev.komsay.panindamobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // 🔹 Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // 🔹 Retrofit + JSON parser
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // 🔹 HTTP logging for debugging
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // 🔹 Optional: charts
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // 🔹 JSON
    implementation("com.google.code.gson:gson:2.11.0")

    // 🔹 Unit testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
