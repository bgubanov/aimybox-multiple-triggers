val componentsVersion: String by rootProject.extra
val aimyboxVersion: String by rootProject.extra

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
}

android {

    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.justai.aimybox.assistant"

        minSdkVersion(21)
        targetSdkVersion(30)

        versionName = componentsVersion
        versionCode = 1
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            //TODO configure pro guard
        }
    }

    packagingOptions {
        pickFirst("lib/x86/libpocketsphinx_jni.so")
        pickFirst("lib/x86_64/libpocketsphinx_jni.so")
        pickFirst("lib/armeabi-v7a/libpocketsphinx_jni.so")
        pickFirst("lib/arm64-v8a/libpocketsphinx_jni.so")
    }

    lintOptions {
        isCheckAllWarnings = true
        isWarningsAsErrors = false
        isAbortOnError = true
    }
}

repositories {
    mavenLocal()
    google()
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
}

dependencies {

//    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.0-beta-3")

    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")
    implementation("com.google.android.material:material:1.0.0")

    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.1")

//    implementation("com.justai.aimybox:components:$componentsVersion")
    implementation(project(":components"))
    implementation("com.justai.aimybox:core:$aimyboxVersion")
    implementation("com.justai.aimybox:google-platform-speechkit:$aimyboxVersion")
    implementation("com.justai.aimybox:yandex-speechkit:$aimyboxVersion")
//    implementation("com.justai.aimybox:pocketsphinx-speechkit:$aimyboxVersion")
//    implementation("com.justai.aimybox:kaldi-speechkit:$aimyboxVersion")
    implementation(project(":kaldi-speechkit"))
//    implementation("com.justai.aimybox:pocketsphinx-android-lib:1.0.0")
}
