plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)


    //alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.example.projectobcane"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.projectobcane"
        minSdk = 26
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
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.navigation.compose)

    // Room
    implementation(libs.room.ktx)
    implementation(libs.room.viewmodel)
    implementation(libs.room.lifecycle)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler.ksp)


    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    ksp(libs.hilt.compiler.ksp)

    // Google Maps
    implementation(libs.googlemap)
    implementation(libs.googlemap.compose)
    implementation(libs.googlemap.foundation)

    implementation(libs.moshi)
    ksp(libs.moshi.ksp)



    implementation(libs.datastore.core)
    implementation(libs.datastore.preferences)



    implementation(libs.compose.colorpicker)

    implementation(libs.appcompat)

    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp( "com.github.bumptech.glide:compiler:4.16.0" )// if you're using annotation processing



    implementation("com.afollestad.material-dialogs:core:3.3.0")


}