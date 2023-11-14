import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}


android {
    namespace = "com.hyejeanmoon.splashcompose"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.hyejeanmoon.splashcompose"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 14
        versionName = "1.4.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties()
        if (rootProject.file("local.properties").exists()) {
            properties.load(rootProject.file("local.properties").inputStream())
        }

        resValue("string", "UNSPLASH_CLIENT_ID", properties.getProperty("UNSPLASH_CLIENT_ID", ""))

    }

    buildTypes {
        release {
            // if minifyEnabled is true, the app will crash. it need to be fixed.
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    kapt {
        correctErrorTypes = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)

    testImplementation(libs.junit)



    // Material design icons
    implementation(libs.androidx.compose.material.iconsCore)
    implementation(libs.androidx.compose.material.iconsExtended)


    // Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewModelCompose )
    // Integration with observables
    implementation(libs.androidx.compose.runtime.livedata)



    // constraintLayout
    implementation(libs.androidx.constraintlayout.compose )


    implementation(libs.google.gson)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson )

    // Timber
    implementation(libs.timber)

    // Jetpack Navigation
    implementation(libs.androidx.navigation.compose);
    // Jetpack Paging3
//    implementation "androidx.paging:paging-compose:1.0.0-alpha18"
    implementation(libs.androidx.paging.compose)

    // Jetpack Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)


    // ROOM
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)


    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)

    // Pager
     implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.insets.ui)

    // coil
    implementation(libs.coil.kt.compose )

    // glide
    implementation(libs.glide )
    kapt(libs.glide.compiler )


    coreLibraryDesugaring(libs.core.jdk.desugaring)

}