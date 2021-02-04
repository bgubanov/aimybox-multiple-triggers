val aimyboxVersion: String by rootProject.extra

plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        isCheckAllWarnings = true
        isWarningsAsErrors = false
        isAbortOnError = true
    }
}

dependencies {
    implementation("com.justai.aimybox:core:$aimyboxVersion")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.1")


//    implementation("org.kaldi:kaldi-android:5.2")
    implementation("com.alphacep:vosk-android:0.3.17")
    implementation("com.neovisionaries:nv-websocket-client:2.9")
}
