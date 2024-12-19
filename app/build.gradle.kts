plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    // room, ksp
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.app.java.trackingrunningapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.app.java.trackingrunningapp"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    // schema
    room {
        schemaDirectory("$projectDir/schemas")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(libs.barchart)
    implementation(libs.circle.image)
    implementation(libs.image.slider)
    implementation(libs.glide)
    // room database
    implementation(libs.room.kotlin)
    implementation(libs.room.runtime)
    // ksp
    ksp(libs.room.compiler)
    // lifecycle ktx
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // map
    implementation(libs.mapbox)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.services.map)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.runtime)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}