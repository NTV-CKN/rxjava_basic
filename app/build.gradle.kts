plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.rxjava_java"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.rxjava_java"
        minSdk = 26
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
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    //glide
    implementation (libs.glide)

    //ImagePicker
    implementation (libs.imagepicker)

    // room database
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // room support rxjava3
    implementation(libs.room.rxjava3)

    // rxjava3
    implementation(libs.rxandroid)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}