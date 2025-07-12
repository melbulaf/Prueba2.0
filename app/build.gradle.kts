plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.shivaishta.prueba20"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.shivaishta.prueba20"
        minSdk = 25
        targetSdk = 36
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    //Para Animaciones
    implementation(libs.lottie)
    implementation("com.itextpdf:itextpdf:5.5.13.2")
    implementation("com.karumi:dexter:6.2.3")
    implementation("com.itextpdf:itextpdf:5.5.13.2")
    // Para permisos
    implementation("com.itextpdf:itextpdf:5.5.13.2")
    implementation("androidx.core:core:1.6.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
}